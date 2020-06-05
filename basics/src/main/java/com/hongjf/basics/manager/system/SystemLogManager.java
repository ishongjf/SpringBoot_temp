package com.hongjf.basics.manager.system;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hongjf.basics.dao.system.SystemLogMapper;
import com.hongjf.basics.entity.system.SystemLogEntity;
import com.hongjf.basics.manager.BaseManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: Hongjf
 * @Date: 2020/1/7
 * @Time: 9:32
 * @Description:
 */
@Repository
public class SystemLogManager implements BaseManager<SystemLogEntity> {

    @Autowired
    private SystemLogMapper systemLogMapper;

    @Override
    public void insert(SystemLogEntity systemLogEntity) {
        systemLogMapper.insert(systemLogEntity);
    }

    @Override
    public void deleteByPrimaryKey(Long id) {
        systemLogMapper.deleteById(id);
    }

    @Override
    public List<SystemLogEntity> selectAll() {
        QueryWrapper<SystemLogEntity> queryWrapper = new QueryWrapper<>();
        return systemLogMapper.selectList(queryWrapper);
    }

    @Override
    public void update(SystemLogEntity systemLogEntity) {
        systemLogMapper.updateById(systemLogEntity);
    }

    @Override
    public SystemLogEntity selectByPrimaryKey(Long id) {
        return systemLogMapper.selectById(id);
    }
}
