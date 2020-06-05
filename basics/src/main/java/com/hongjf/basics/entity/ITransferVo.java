package com.hongjf.basics.entity;

/**
 * @Author: Hongjf
 * @Date: 2020/1/7
 * @Time: 17:13
 * @Description:entity转vo
 */
public interface ITransferVo<T> {
    /**
     * entity -> vo
     *
     * @return
     */
    T transferToVo();
}
