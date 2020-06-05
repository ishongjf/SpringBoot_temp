package com.hongjf.common.utils;

import com.hongjf.common.enums.global.ConditionContentEnum;

import java.math.BigDecimal;

/**
 * @Author: hongjf
 * @Date: 2020/6/5
 * @Time: 20:54
 * @Description:BigDecima运算工具类
 */
public class BigDecimalUtil {
    // 除法运算默认精度
    private static final int DEF_DIV_SCALE = 10;

    private BigDecimalUtil() {

    }

    /**
     * 精确加法
     */
    public static double add(double value1, double value2) {
        BigDecimal b1 = BigDecimal.valueOf(value1);
        BigDecimal b2 = BigDecimal.valueOf(value2);
        return b1.add(b2).doubleValue();
    }

    /**
     * 精确加法
     */
    public static double add(String value1, String value2) {
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        return b1.add(b2).doubleValue();
    }

    /**
     * 精确减法
     */
    public static double sub(double value1, double value2) {
        BigDecimal b1 = BigDecimal.valueOf(value1);
        BigDecimal b2 = BigDecimal.valueOf(value2);
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 精确减法
     */
    public static double sub(String value1, String value2) {
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 精确乘法
     */
    public static double mul(double value1, double value2) {
        BigDecimal b1 = BigDecimal.valueOf(value1);
        BigDecimal b2 = BigDecimal.valueOf(value2);
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 精确乘法
     */
    public static double mul(String value1, String value2) {
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 精确除法 使用默认精度
     */
    public static double div(double value1, double value2) throws IllegalAccessException {
        return div(value1, value2, DEF_DIV_SCALE);
    }

    /**
     * 精确除法 使用默认精度
     */
    public static double div(String value1, String value2) throws IllegalAccessException {
        return div(value1, value2, DEF_DIV_SCALE);
    }

    /**
     * 精确除法
     *
     * @param scale 精度
     */
    public static double div(double value1, double value2, int scale) throws IllegalAccessException {
        if (scale < 0) {
            throw new IllegalAccessException("精确度不能小于0");
        }
        BigDecimal b1 = BigDecimal.valueOf(value1);
        BigDecimal b2 = BigDecimal.valueOf(value2);
        // return b1.divide(b2, scale).doubleValue();
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 精确除法
     *
     * @param scale 精度
     */
    public static double div(String value1, String value2, int scale) throws IllegalAccessException {
        if (scale < 0) {
            throw new IllegalAccessException("精确度不能小于0");
        }
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        // return b1.divide(b2, scale).doubleValue();
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 四舍五入
     *
     * @param scale 小数点后保留几位
     */
    public static double round(double v, int scale) throws IllegalAccessException {
        return div(v, 1, scale);
    }

    /**
     * 四舍五入
     *
     * @param scale 小数点后保留几位
     */
    public static double round(String v, int scale) throws IllegalAccessException {
        return div(v, "1", scale);
    }

    /**
     * 比较大小
     */
    public static boolean equalTo(BigDecimal b1, BigDecimal b2) {
        if (b1 == null || b2 == null) {
            return false;
        }
        return 0 == b1.compareTo(b2);
    }


    /**
     * 比较大小
     *
     * @param compared     被对比数
     * @param condition    条件
     * @param compareValue 对比值
     * @return
     */
    public static boolean comparative(long compared, long condition, long compareValue) {
        ConditionContentEnum conditionContentEnum = ConditionContentEnum.codeOf(Math.toIntExact(condition));
        boolean flag = false;
        switch (conditionContentEnum) {

            case GREATER_THAN:
                //大于
                flag = compared > compareValue ? true : false;
                break;
            case EQUAL_TO:
                //等于
                flag = compared == compareValue ? true : false;
                break;
            case LESS_THAN:
                //小于
                flag = compared < compareValue ? true : false;
                break;
            case GREATER_EQUAL:
                //大于等于
                flag = compared >= compareValue ? true : false;
                break;
            case LESS_EQUAL:
                //小于等于
                flag = compared <= compareValue ? true : false;
                break;
            default:
        }
        return flag;

    }

    /**
     * 比较大小
     *
     * @param compared     被对比数
     * @param condition    条件
     * @param compareValue 对比值
     * @return
     */
    public static boolean comparative(double compared, long condition, double compareValue) {
        ConditionContentEnum conditionContentEnum = ConditionContentEnum.codeOf(Math.toIntExact(condition));
        boolean flag = false;
        switch (conditionContentEnum) {

            case GREATER_THAN:
                //大于
                flag = compared > compareValue ? true : false;
                break;
            case EQUAL_TO:
                //等于
                flag = compared == compareValue ? true : false;
                break;
            case LESS_THAN:
                //小于
                flag = compared < compareValue ? true : false;
                break;
            case GREATER_EQUAL:
                //大于等于
                flag = compared >= compareValue ? true : false;
                break;
            case LESS_EQUAL:
                //小于等于
                flag = compared <= compareValue ? true : false;
                break;
            default:
        }
        return flag;

    }
}
