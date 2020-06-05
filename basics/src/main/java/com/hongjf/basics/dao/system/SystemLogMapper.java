package com.hongjf.basics.dao.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hongjf.basics.entity.system.SystemLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: Hongjf
 * @Date: 2020/1/7
 * @Time: 9:31
 * @Description:
 */
@Mapper
public interface SystemLogMapper extends BaseMapper<SystemLogEntity> {
}
