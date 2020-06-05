package com.hongjf.common.config;

import cn.hutool.core.util.IdUtil;
import com.aliyun.oss.*;
import com.aliyun.oss.model.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

/**
 *
 *
 * @Author: Hongjf
 * @Date: 2020/1/14
 * @Time: 14:46
 * @Description:oss配置工具类
 */
@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "aliyun.oss")
public class OssConfig {

    /**
     * oss的endpoint
     */
    private String endpoint;
    /**
     * oss的accessKeyId
     */
    private String accessKeyId;
    /**
     * oss的accessKeySecret
     */
    private String accessKeySecret;
    /**
     * oss的bucket
     */
    private String bucket;
    /**
     * resUrl
     */
    public String resUrl;

    /**
     * 获取线程池
     *
     * @return
     */
    public static ThreadPoolExecutor getPool() {
        ThreadFactory namedThreadFactory = new BasicThreadFactory.Builder().namingPattern(
                "oss-pool-%d").daemon(true).build();
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
                10, 10, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),
                namedThreadFactory,
                new ThreadPoolExecutor.DiscardOldestPolicy());
        return threadPool;
    }

    ////////////////////////////////////////分片上传参数/////////////////////////////////

    private static String KEY = null;
    private static OSS OSS_CLIENT = null;
    /**
     * 分片上传事件的唯一标识
     */
    private static String UPLOAD_ID = null;
    /**
     * 定长线程池
     */
    private ExecutorService threadPool = getPool();
    /**
     * 分片集合
     */
    private List<PartETag> partETags = null;
    /**
     * 每次上传大小
     */
    private final long PART_SIZE = 1 * 1024 * 1024L;

    /**
     * 单一上传
     *
     * @param file     要上传的文件
     * @param filePath 文件存放路径
     * @return
     */
    public String upload(File file, String filePath) {
        log.info("=========>OSS文件上传开始：{}", file.getName());
        String endpoint = this.endpoint;
        String accessKeyId = this.accessKeyId;
        String accessKeySecret = this.accessKeySecret;
        String bucketName = this.bucket;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = format.format(new Date());
        if (null == file) {
            return null;
        }
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        try {
            //容器不存在，就创建
            if (!ossClient.doesBucketExist(bucketName)) {
                ossClient.createBucket(bucketName);
                CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
                createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
                ossClient.createBucket(createBucketRequest);
            }
            //创建文件路径
            //上传文件
            String fileUrl = filePath + "/" + dateStr + "/" + UUID.randomUUID().toString().replace("-", "") + "-" + file.getName();
            //设置权限 这里是公开读
            PutObjectResult result = ossClient.putObject(new PutObjectRequest(bucketName, fileUrl, file));
            ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
            if (null != result) {
                log.info("==========>OSS文件上传成功,OSS地址：{}", fileUrl);
                return resUrl + fileUrl;
            }
        } catch (OSSException oe) {
            log.error(">>>>>>>>>OSS上传异常,文件名:", file.getName(), oe);
        } catch (ClientException ce) {
            log.error(">>>>>>>>>OSS上传异常,文件名:", file.getName(), ce);
        } finally {
            //关闭
            ossClient.shutdown();
        }
        return null;
    }

    /**
     * 分片上传入口
     *
     * @param file     上传的文件
     * @param filePath 上传路径
     * @return
     */
    public String fileUpload(File file, String filePath) {
        long start = System.currentTimeMillis();
        log.info("================>OSS文件上传开始:{}", file.getName());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = format.format(new Date());
        filePath = filePath + "/" + dateStr + "/" + IdUtil.randomUUID() + "-" + file.getName();
        // 初始化一个分片上传事件
        KEY = filePath;
        // 返回uploadId，它是分片上传事件的唯一标识，可以根据这个ID来发起相关的操作，如取消分片上传、查询分片上传等。
        UPLOAD_ID = claimUploadId();
        try {
            upload(file, filePath, UPLOAD_ID);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭OSSClient。
            OSS_CLIENT.shutdown();
            partETags.clear();
            partETags = null;
            KEY = null;
            UPLOAD_ID = null;
            file.delete();
        }
        log.info("上传结束，上传地址为:" + filePath + ",共用时" + (System.currentTimeMillis() - start) + "毫秒");
        return "https://" + bucket + "." + endpoint + "/" + filePath;
    }

    /**
     * 分片上传处理
     *
     * @param file     要上传的文件
     * @param filePath 上传的路径
     * @param uploadId 唯一id
     * @throws Exception
     */
    private void upload(File file, String filePath, String uploadId) throws Exception {
        // 计算文件有多少个分片
        long fileLength = file.length();
        int partCount = (int) (fileLength / PART_SIZE);
        if (fileLength % PART_SIZE != 0) {
            partCount++;
        }
        if (partCount > 10000) {
            log.warn("partCount总数不应超过10000");
            throw new Exception("partCount总数不应超过10000");
        } else {
            log.info("文件总共分片数:" + partCount);
        }
        //初始化集合线程池
        partETags = Collections.synchronizedList(new ArrayList<PartETag>(partCount));
        CountDownLatch latch = new CountDownLatch(partCount);
        // 遍历分片上传。
        for (int i = 0; i < partCount; i++) {
            long startPos = i * PART_SIZE;
            long curPartSize = (i + 1 == partCount) ? (fileLength - startPos) : PART_SIZE;
            threadPool.execute(new PartUploader(file, startPos, curPartSize, i + 1, latch));
        }
        latch.await();
        if (partETags.size() != partCount) {
            StringBuilder partETagsStr = new StringBuilder("(");
            for (PartETag item : partETags) {
                partETagsStr.append(item.getPartNumber()).append(",");
            }
            partETagsStr.append(")");
            log.info("partCount:[{}],partEtages:[{}],partETagsSize:[{}]", partCount, partETagsStr, partETags.size());
            throw new IllegalStateException("上传多个部分失败，因为有些部分还没有完成");
        } else {
            log.info("成功地将多个部分合并上传到一个名为的对象中 " + KEY);
        }
        listAllParts(uploadId);
        completeMultipartUpload();
        // 生成文件地址
        boolean isFileExist = OSS_CLIENT.doesObjectExist(bucket, filePath);
        if (!isFileExist) {
            throw new Exception("上传文件失败");
        }

    }

    /**
     * 处理分片集合 排序
     */
    private void completeMultipartUpload() {
        // 排序 partETags必须按分片号升序排列。
        Collections.sort(partETags, new Comparator<PartETag>() {
            @Override
            public int compare(PartETag p1, PartETag p2) {
                return p1.getPartNumber() - p2.getPartNumber();
            }
        });
        // 在执行该操作时，需要提供所有有效的partETags。OSS收到提交的partETags后，会逐一验证每个分片的有效性。当所有的数据分片验证通过后，OSS将把这些分片组合成一个完整的文件。
        CompleteMultipartUploadRequest completeMultipartUploadRequest =
                new CompleteMultipartUploadRequest(bucket, KEY, UPLOAD_ID, partETags);
        OSS_CLIENT.completeMultipartUpload(completeMultipartUploadRequest);
    }

    /**
     * 生成upLoadId
     *
     * @return
     */
    private String claimUploadId() {
        ClientConfiguration conf = new ClientConfiguration();
        conf.setIdleConnectionTime(5000);
        OSS_CLIENT = new OSSClient(endpoint, accessKeyId, accessKeySecret, conf);
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucket, KEY);
        request.addHeader("Cache-Control", "no-cache");
        InitiateMultipartUploadResult result = OSS_CLIENT.initiateMultipartUpload(request);
        return result.getUploadId();
    }

    private void listAllParts(String uploadId) {
        log.info("Listing all parts......");
        ListPartsRequest listPartsRequest = new ListPartsRequest(bucket, KEY, uploadId);
        PartListing partListing = OSS_CLIENT.listParts(listPartsRequest);

        int partCount = partListing.getParts().size();
        for (int i = 0; i < partCount; i++) {
            PartSummary partSummary = partListing.getParts().get(i);
            log.info("Part:{}, ETag:{}", partSummary.getPartNumber(), partSummary.getETag());
        }
    }

    /**
     * 静态内部类，上传组件
     */
    private class PartUploader implements Runnable {
        private File localFile;
        private long partSize;
        private int partNumber;
        private long startPos;
        private CountDownLatch latch;

        public PartUploader(File localFile, long startPos, long partSize, int partNumber, CountDownLatch latch) {
            this.localFile = localFile;
            this.partSize = partSize;
            this.partNumber = partNumber;
            this.startPos = startPos;
            this.latch = latch;
        }

        @Override
        public void run() {
            log.info(">>>>>>>>>>>>>>>>>>>>>开始上传第[{}]片<<<<<<<<<<<<<<<<<<<<<<<<", partNumber);
            InputStream instream = null;
            try {
                instream = new FileInputStream(localFile);
                // 跳过已经上传的分片。
                instream.skip(startPos);
                UploadPartRequest uploadPartRequest = new UploadPartRequest();
                uploadPartRequest.setBucketName(bucket);
                uploadPartRequest.setKey(KEY);
                uploadPartRequest.setUploadId(UPLOAD_ID);
                uploadPartRequest.setInputStream(instream);
                // 设置分片大小,除了最后一个分片没有大小限制，其他的分片最小为100KB。
                uploadPartRequest.setPartSize(partSize);
                // 设置分片号,每一个上传的分片都有一个分片号，取值范围是1~10000，如果超出这个范围，OSS将返回InvalidArgument的错误码。
                uploadPartRequest.setPartNumber(partNumber);
                // 每个分片不需要按顺序上传,甚至可以在不同客户端上传，OSS会按照分片号排序组成完整的文件。
                UploadPartResult uploadPartResult = OSS_CLIENT.uploadPart(uploadPartRequest);
                // 每次上传分片之后,OSS的返回结果会包含一个PartETag,PartETag将被保存到partETags中。
                synchronized (partETags) {
                    partETags.add(uploadPartResult.getPartETag());
                }
            } catch (Exception e) {
                log.error(">>>>>>>>>>>>>>>>>分片上传错误，第[{}]片", partNumber, e);
            } finally {
                if (instream != null) {
                    try {
                        instream.close();
                    } catch (IOException e) {
                        log.error(">>>>>>>>>>>>>>>>>输入流关闭错误，第[{}]片", partNumber, e);
                    }
                }
                latch.countDown();
            }
        }
    }


}
