package com.hongjf.common.utils;

import com. hongjf.common.enums.global.FileTypeEnum;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;

/**
 * Copyright 2019  hongjf, Inc. All rights reserved.
 *
 * @Author: Hongjf
 * @Date: 2020/5/7
 * @Time: 15:40
 * @Description:通过文件魔数，判断文件类型
 */
@Slf4j
public class FileTypeUtil {

    /**
     * 获取图片文件实际类型,若不是图片则返回null
     *
     * @param file
     * @return fileType
     */
    public final static String getImageFileType(File file) {
        if (isImage(file)) {
            try {
                ImageInputStream iis = ImageIO.createImageInputStream(file);
                Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
                if (!iter.hasNext()) {
                    return null;
                }
                ImageReader reader = iter.next();
                iis.close();
                return reader.getFormatName();
            } catch (IOException e) {
                log.error(">>>>>>>>>>文件解析异常", e);
                return null;
            } catch (Exception e) {
                log.error(">>>>>>>>>>文件解析异常", e);
                return null;
            }
        }
        return null;
    }

    /**
     * 获取文件类型,包括图片,若格式不是已配置的,则返回null
     *
     * @param file
     * @return fileType
     */
    public final static String getFileTypeByFile(File file) {
        String filetype = null;
        byte[] b = new byte[50];
        try {
            InputStream is = new FileInputStream(file);
            is.read(b);
            filetype = getFileTypeByStream(b);
            is.close();
        } catch (FileNotFoundException e) {
            log.error(">>>>>>>>>>文件解析异常", e);
        } catch (IOException e) {
            log.error(">>>>>>>>>>文件解析异常", e);
        }
        return filetype;
    }

    /**
     * 通过数据流（二进制数据）判断文件类型
     *
     * @param b
     * @return fileType
     */
    public final static String getFileTypeByStream(byte[] b) {
        String magicNumberCode = String.valueOf(getFileHexString(b));
        if (ToolUtil.isNotEmpty(magicNumberCode)) {
            return FileTypeEnum.getByMagicNumberCode(magicNumberCode.toUpperCase()).getFileTypeName();
        }
        return null;
    }

    /**
     * isImage,判断文件是否为图片
     *
     * @param file
     * @return true 是 | false 否
     */
    public static final boolean isImage(File file) {
        boolean flag = false;
        try {
            BufferedImage bufreader = ImageIO.read(file);
            int width = bufreader.getWidth();
            int height = bufreader.getHeight();
            if (width == 0 || height == 0) {
                flag = false;
            } else {
                flag = true;
            }
        } catch (IOException e) {
            log.error(">>>>>>>>>>文件解析异常", e);
            flag = false;
        } catch (Exception e) {
            log.error(">>>>>>>>>>文件解析异常", e);
            flag = false;
        }
        return flag;
    }


    /**
     * 通过文件路径判断文件类型
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static FileTypeEnum getFileTypeByPath(String path) {
        // 获取文件头
        String magicNumberCode = null;
        try {
            magicNumberCode = getFileHeader(path);
        } catch (Exception e) {
            log.error(">>>>>>>>>>文件解析异常", e);
            return null;
        }
        if (ToolUtil.isNotEmpty(magicNumberCode)) {
            return FileTypeEnum.getByMagicNumberCode(magicNumberCode.toUpperCase());
        }
        return null;
    }


    /**
     * 通过文件路径获取文件头（即文件魔数）
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static String getFileHeader(String path) throws Exception {
        byte[] b = new byte[28];
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(path);
            inputStream.read(b, 0, 28);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return getFileHexString(b);
    }

    /**
     * 把文件二进制流转换成十六进制数据
     *
     * @param b
     * @return fileTypeHex
     */
    public final static String getFileHexString(byte[] b) {
        StringBuilder builder = new StringBuilder();
        if (b == null || b.length <= 0) {
            return null;
        }
        for (int i = 0; i < b.length; i++) {
            int v = b[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                builder.append(0);
            }
            builder.append(hv);
        }
        return builder.toString();
    }

}
