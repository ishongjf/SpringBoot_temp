package com.hongjf.basics.manager.base;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hongjf.basics.dao.base.UserMapper;
import com.hongjf.basics.entity.base.UserEntity;
import com.hongjf.basics.manager.BaseManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: hongjf
 * @Date: 2020/2/12
 * @Time: 13:47
 * @Description:用户表manager
 */
@Repository
public class UserManager implements BaseManager<UserEntity> {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void insert(UserEntity userEntity) {
        userMapper.insert(userEntity);
    }

    @Override
    public void deleteByPrimaryKey(Long id) {
        userMapper.deleteById(id);
    }

    @Override
    public List<UserEntity> selectAll() {
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        return userMapper.selectList(queryWrapper);
    }

    @Override
    public void update(UserEntity appletUserEntity) {
        appletUserEntity.setUpdateTime(null);
        userMapper.updateById(appletUserEntity);
    }

    @Override
    public UserEntity selectByPrimaryKey(Long id) {
        return userMapper.selectById(id);
    }


}
