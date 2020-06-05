package com.hongjf.basics.manager;

import com.hongjf.basics.entity.BaseEntity;

import java.util.List;

/**
 * @Author: Hongjf
 * @Date: 2020/1/7
 * @Time: 11:35
 * @Description:
 */
public interface BaseManager<T extends BaseEntity> {
    /**
     * 新增
     *
     * @param t 保存的实体类
     */
    void insert(T t);

    /**
     * 根据主键删除
     *
     * @param id 主键id
     */
    void deleteByPrimaryKey(Long id);

    /**
     * 查询所有
     *
     * @return
     */
    List<T> selectAll();

    /**
     * 更新实体类
     *
     * @param t
     */
    void update(T t);

    /**
     * 根据id查询指定数据
     *
     * @param id
     * @return
     */
    T selectByPrimaryKey(Long id);
}
