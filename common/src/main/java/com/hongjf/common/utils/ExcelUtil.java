package com.hongjf.common.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com. hongjf.common.enums.global.FileTypeEnum;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileExistsException;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Copyright 2019  hongjf, Inc. All rights reserved.
 *
 * @Author: Hongjf
 * @Date: 2019/12/10
 * @Time: 16:53
 * @Description:
 */
@Slf4j
public class ExcelUtil {

    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final int DEFAULT_COLUMN_WIDTH = 20;

    private static final String EXCEL_XLS = ".xls";
    private static final String EXCEL_XLSX = ".xlsx";

    private static final String TITLE = "title";

    /**
     * 导出Excel(.xlsx)格式
     *
     * @param titleList 表格头信息集合
     * @param dataArray 数据数组
     * @param
     */
    public static void exportExcel(List<List<LinkedHashMap>> titleList, List<JSONArray> dataArray, HttpServletResponse response, HttpServletRequest request, String name) {

        String datePattern = DEFAULT_DATE_PATTERN;
        int minBytes = DEFAULT_COLUMN_WIDTH;

        //声明一个工作薄
        SXSSFWorkbook workbook = new SXSSFWorkbook(5000);
        workbook.setCompressTempFiles(true);

        // 表头1样式
        CellStyle title1Style = workbook.createCellStyle();
        title1Style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        title1Style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        Font titleFont = workbook.createFont();
        titleFont.setFontHeightInPoints((short) 20);
        titleFont.setBoldweight((short) 200);
        title1Style.setFont(titleFont);

        // head样式
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        headerStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        headerStyle.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
        headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        Font headerFont = workbook.createFont();
        headerFont.setFontHeightInPoints((short) 15);
        headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headerStyle.setFont(headerFont);

        // 单元格样式
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        Font cellFont = workbook.createFont();
        cellFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        cellStyle.setFont(cellFont);


        for (int i = 0; i < titleList.size(); i++) {
            List<LinkedHashMap> list = titleList.get(i);
            String title1 = (String) list.get(0).get(TITLE);//标题
            LinkedHashMap<String, String> headMap = list.get(1);//表头
            JSONArray objects = dataArray.get(i);
            excelSheet(objects, datePattern, minBytes, workbook, title1Style, headerStyle, cellStyle, title1, headMap, i);
        }

        OutputStream output;
        try {
            String agent = request.getHeader("USER-AGENT").toLowerCase();
            response.setContentType("application/json;charset=UTF-8");
            String fileName = name + EXCEL_XLSX;
            String codedFileName = java.net.URLEncoder.encode(fileName, "UTF-8");
            if (agent.contains("firefox")) {
                response.setCharacterEncoding("utf-8");
                response.setHeader("content-disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
            } else {
                response.setHeader("content-disposition", "attachment;filename=" + codedFileName);
            }
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("FileName", codedFileName);
            response.setHeader("Access-Control-Expose-Headers", "*");
            output = response.getOutputStream();
            BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output);
            bufferedOutPut.flush();
            workbook.write(bufferedOutPut);
            bufferedOutPut.close();
            // 释放workbook所占用的所有windows资源
            workbook.dispose();
        } catch (IOException e) {
            log.error(">>>>>>>>>>>>文件导出失败,文件名[{}]", name, e);
        }
    }

    /**
     * @param dataArray   导出数据
     * @param datePattern
     * @param minBytes
     * @param workbook
     * @param title1Style
     * @param headerStyle
     * @param cellStyle
     * @param title1
     * @param headMap
     */
    private static void excelSheet(JSONArray dataArray, String datePattern, int minBytes, SXSSFWorkbook workbook, CellStyle title1Style, CellStyle headerStyle, CellStyle cellStyle, String title1, LinkedHashMap<String, String> headMap, int num) {
        //生成一个(带名称)表格，名称不能重复
        SXSSFSheet sheet = (SXSSFSheet) workbook.createSheet(title1 + num);
        sheet.createFreezePane(0, 3, 0, 3);

        //生成head相关信息+设置每列宽度
        int[] colWidthArr = new int[headMap.size()];
        String[] headKeyArr = new String[headMap.size()];
        String[] headValArr = new String[headMap.size()];
        int i = 0;
        for (Map.Entry<String, String> entry : headMap.entrySet()) {
            headKeyArr[i] = entry.getKey();
            headValArr[i] = entry.getValue();

            int bytes = headKeyArr[i].getBytes().length;
            colWidthArr[i] = bytes < minBytes ? minBytes : bytes;
            sheet.setColumnWidth(i, colWidthArr[i] * 256);
            //sheet.autoSizeColumn(i, true);
            i++;
        }

        //遍历数据集合，产生Excel行数据
        int rowIndex = 0;
        for (Object obj : dataArray) {
            // 生成title+head信息
            if (rowIndex == 0) {
                SXSSFRow title1Row = (SXSSFRow) sheet.createRow(0);
                title1Row.createCell(0).setCellValue(title1);
                title1Row.getCell(0).setCellStyle(title1Style);
                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headMap.size() - 1));
                SXSSFRow title2Row = (SXSSFRow) sheet.createRow(1);
                CreationHelper createHelper = workbook.getCreationHelper();
                XSSFHyperlink hyperLink = (XSSFHyperlink) createHelper.createHyperlink(Hyperlink.LINK_URL);

                sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, headMap.size() - 1));

                SXSSFRow headerRow = (SXSSFRow) sheet.createRow(2);
                for (int j = 0; j < headValArr.length; j++) {
                    headerRow.createCell(j).setCellValue(headValArr[j]);
                    headerRow.getCell(j).setCellStyle(headerStyle);
                }
                rowIndex = 3;
            }

            JSONObject jo = (JSONObject) JSONObject.toJSON(obj);
            // 生成数据
            SXSSFRow dataRow = (SXSSFRow) sheet.createRow(rowIndex);
            for (int k = 0; k < headKeyArr.length; k++) {
                SXSSFCell cell = (SXSSFCell) dataRow.createCell(k);
                Object o = jo.get(headKeyArr[k]);
                String cellValue = "";
                if (o == null) {
                    cellValue = "";
                } else if (o instanceof Date) {
                    cellValue = new SimpleDateFormat(datePattern).format(o);
                } else if (o instanceof Float || o instanceof Double) {
                    cellValue = new BigDecimal(o.toString()).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                } else {
                    cellValue = o.toString();
                }
                cell.setCellValue(cellValue);
                cell.setCellStyle(cellStyle);
            }
            rowIndex++;
        }
    }

    /**
     * 导出xls
     *
     * @param response
     * @param request
     * @param list     要存放的数据
     * @param title    标题
     * @param headMap  ("表中的字段","显示的字段名")
     * @param filename 设置导出的文件名
     */
    public static void export(HttpServletResponse response, HttpServletRequest request,
                              List<?> list, String title, LinkedHashMap<String, String> headMap, String filename) {
        int count = 0;
        JSONArray orderArray = new JSONArray();
        long startTime = System.currentTimeMillis();
        for (Object object : list) {
            orderArray.add(object);
            count++;
        }
        ArrayList<LinkedHashMap> titleList = new ArrayList<LinkedHashMap>();
        // 1.titleMap存放了该excel的头信息
        LinkedHashMap<String, String> titleMap = new LinkedHashMap<String, String>();
        titleMap.put(TITLE, title);
        // 2.headMap存放了该excel的列项
        titleList.add(titleMap);
        titleList.add(headMap);
        long endTime = System.currentTimeMillis();
        log.info("导出完成..........共" + count + "条数据");
        log.info("程序运行时间：" + (endTime - startTime) + "ms");

        List<JSONArray> resultList = new ArrayList<>();
        resultList.add(orderArray);
        List<List<LinkedHashMap>> title1 = new ArrayList<List<LinkedHashMap>>();
        title1.add(titleList);
        ExcelUtil.exportExcel(title1, resultList, response, request, filename);
    }

    public static void downLoad(HttpServletResponse response, InputStream in, String fileName) throws Exception {
        InputStream inputStream = null;
        OutputStream out = null;
        try {
            String codedFileName = java.net.URLEncoder.encode(fileName, "UTF-8");
            inputStream = in;
            //强制下载不打开
            response.setContentType("application/force-download");
            out = response.getOutputStream();
            //使用URLEncoder来防止文件名乱码或者读取错误
            response.setHeader("Content-Disposition", "attachment;filename=" + codedFileName);
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("FileName", codedFileName);
            response.setHeader("Access-Control-Expose-Headers", "*");
            int b = 0;
            byte[] buffer = new byte[1000000];
            while (b != -1) {
                b = inputStream.read(buffer);
                if (b != -1) {
                    out.write(buffer, 0, b);
                }
            }
            inputStream.close();
            out.close();
            out.flush();
        } catch (Exception e) {
            log.error(">>>>>>>>>>>>>导入模板下载失败，文件名：[{}]", fileName, e);
            throw new Exception("导入模板下载失败");
        } finally {
            inputStream.close();
            out.close();
        }
    }


    /**
     * excel导入
     *
     * @param file   excel文件
     * @param column 导入字段
     */
    public static Map<String, Object> importExcel(MultipartFile file, Map<String, ColumnInfo> column) throws Exception {
        File excel = multipartFileToFile(file);
        //判断文件
        checkExcel(excel);
        //导入失败总行数
        int impError = 0;
        //异常信息
        StringBuffer impErrorLog = new StringBuffer();
        //开始处理excel
        Workbook workbook = getWorkBook(excel);
        if (workbook == null){
            excel.delete();
            throw new Exception("workbook为空!");
        }
        Sheet sheet = workbook.getSheetAt(0);
        if (sheet == null) {
            excel.delete();
            throw new Exception("sheet为空!");
        }
        Map<String, Object> result = new HashMap<>(16);
        List<JSONObject> jsonObjects = new ArrayList<>(10);
        try {
            for (int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row rows = sheet.getRow(rowIndex);
                if (rows == null) {
                    continue;
                }
                //第一行表头
                if (rowIndex == 0) {
                    int firstCell = rows.getFirstCellNum();
                    int lastCell = rows.getLastCellNum();
                    for (int cellIndex = firstCell; cellIndex < lastCell; cellIndex++) {
                        Cell cell = rows.getCell(cellIndex);
                        if (cell != null) {
                            String columnValue = cell.getStringCellValue();
                            columnValue = (columnValue == null) ? "" : columnValue.trim();
                            if (!sheet.isColumnHidden(cellIndex) && columnValue.length() > 0) {
                                for (String key : column.keySet()) {
                                    ColumnInfo ic = column.get(key);
                                    if ((ic.getXlsTitle().equalsIgnoreCase(columnValue))) {
                                        ic.setXlsCols(cellIndex);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                //从第二行开始获取导入数据
                if (rowIndex != 0) {
                    try {
                        JSONObject jsonObject = cellValue(column, rows);
                        jsonObjects.add(jsonObject);
                    } catch (Exception e) {
                        impError++;
                        impErrorLog.append("出错行数:").append(rowIndex).append(",").append("错误原因:").append(e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            log.error(">>>>>>>>>>>>excel文件导入解析失败", e);
            excel.delete();
            throw e;
        } finally {
            excel.delete();
        }
        log.info("导入总行数:" + impError);
        log.info("导入错误信息:" + impErrorLog.toString());
        result.put("data", jsonObjects);
        result.put("error", impErrorLog.toString());
        return result;
    }

    /**
     * 判断文件是否合理
     *
     * @param file
     * @throws Exception
     */
    public static void checkExcel(File file) throws Exception {
        if (!file.exists()) {
            throw new FileExistsException("文件不存在!");
        }
        if (!file.isFile()) {
            throw new FileExistsException("文件不存在!");
        }
        if (!file.getName().endsWith(EXCEL_XLS) && !file.getName().endsWith(EXCEL_XLSX)) {
            throw new Exception("文件类型异常,请检查!");
        }
    }

    /**
     * 判断excel的版本，并根据文件流数据获取workbook
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static Workbook getWorkBook(File file) throws Exception {
        Workbook workbook = null;
        InputStream inputStream = new FileInputStream(file);
        //通过文件魔数判断文件类型
        String fileType = FileTypeUtil.getFileTypeByFile(file);
        if (FileTypeEnum.XLS.getFileTypeName().equals(fileType)
                || FileTypeEnum.DOC.getFileTypeName().equals(fileType)) {
            workbook = new HSSFWorkbook(inputStream);
        } else if (FileTypeEnum.XLSX.getFileTypeName().equals(fileType)
                || FileTypeEnum.DOCX.getFileTypeName().equals(fileType)) {
            workbook = new XSSFWorkbook(inputStream);
        }
        //通过文件后缀名判断文件类型，但是容易被改后缀造成系统异常
        /*if (file.getName().endsWith(EXCEL_XLS)) {
            workbook = new HSSFWorkbook(inputStream);
        } else if (file.getName().endsWith(EXCEL_XLSX)) {
            workbook = new XSSFWorkbook(inputStream);
        }*/
        return workbook;
    }

    /**
     * 获取行数据
     *
     * @param columns
     * @param rows
     * @return
     */
    public static JSONObject cellValue(Map<String, ColumnInfo> columns, Row rows) throws Exception {
        SimpleDateFormat df;
        NumberFormat nf = new DecimalFormat("#.####");
        JSONObject jsonObject = new JSONObject();
        for (String key : columns.keySet()) {
            ColumnInfo ic = columns.get(key);
            if (ic.getXlsCols() == -1 || ic.getXlsTitle().length() == 0) {
                continue;
            }
            String v = "";
            Cell cell = rows.getCell(ic.getXlsCols());
            if (cell != null) {
                try {
                    // 数值
                    if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                        if (HSSFDateUtil.isCellDateFormatted(cell)) {
                            if (ic.getXlsType().equalsIgnoreCase("Date")) {
                                df = new SimpleDateFormat("yyyy-MM-dd");
                                v = df.format(cell.getDateCellValue());
                            } else if (ic.getXlsType().equalsIgnoreCase("Datetime")) {
                                df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                v = df.format(cell.getDateCellValue());
                            } else {
                                v = cell.getStringCellValue();
                            }
                        } else {
                            v = nf.format(cell.getNumericCellValue());
                        }
                        // 公式
                    } else if (cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
                        try {
                            v = nf.format(cell.getNumericCellValue());
                        } catch (Exception e) {
                            v = cell.getStringCellValue();
                        }
                    } else {
                        v = cell.getStringCellValue();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new Exception("获取单元格失败");
                }
                if (v != null) {
                    v = v.trim();
                }
                if (ic.getXlsType().equalsIgnoreCase("Boolean") && v.length() > 1) {
                    v = v.substring(0, 1);
                }
            }
            if (ic.getXlsType().equalsIgnoreCase("Number") && v.length() == 0) {
                v = "0";
            }
            jsonObject.put(ic.getColumn(), v);
        }
        return jsonObject;
    }

    public static File multipartFileToFile(MultipartFile multipartFile) throws Exception {
        if (ToolUtil.isEmpty(multipartFile)) {
            throw new Exception("文件解析失败");
        }
        try {
            String filename = multipartFile.getOriginalFilename();
            if (!"".equals(filename.trim())) {
                File newFile = new File(filename);
                FileOutputStream os = new FileOutputStream(newFile);
                os.write(multipartFile.getBytes());
                os.close();
                multipartFile.transferTo(newFile);
                return newFile;
            } else {
                throw new Exception("文件解析失败");
            }
        } catch (Exception e) {
            throw new Exception("文件解析失败");
        }
    }

    @Data
    public static class ColumnInfo {
        /**
         * 列号
         */
        private int xlsCols;
        /**
         * 字段名称
         */
        private String column;
        /**
         * excel表头
         */
        private String xlsTitle;
        /**
         * 字段类型
         */
        private String xlsType;
    }
}
