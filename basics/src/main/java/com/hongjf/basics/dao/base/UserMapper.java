package com.hongjf.basics.dao.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hongjf.basics.entity.base.UserEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: Hongjf
 * @Date: 2020/5/15
 * @Time: 17:36
 * @Description:用户mapper
 */
@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {
}
