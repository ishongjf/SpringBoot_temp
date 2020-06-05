package com.hongjf.common.sms.init;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * Copyright 2020  hongjf, Inc. All rights reserved.
 *
 * @Author: Hongjf
 * @Date: 2020/5/18
 * @Time: 10:56
 * @Description:短信信息表 Mapper 接口
 */
@Mapper
public interface SmsInfoMapper extends BaseMapper<SmsInfoEntity> {
}
