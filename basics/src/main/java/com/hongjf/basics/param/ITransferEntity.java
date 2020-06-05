package com.hongjf.basics.param;

/**
 * @Author: Hongjf
 * @Date: 2020/1/7
 * @Time: 17:12
 * @Description:param转entity
 */
public interface ITransferEntity<T> {
    /**
     * param -> entity
     *
     * @return
     */
    T transferToEntity();
}
