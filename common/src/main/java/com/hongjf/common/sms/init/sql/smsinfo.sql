CREATE TABLE `sys_sms_info` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `phone_numbers` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '手机号',
  `validate_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '短信验证码',
  `template_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '短信模板ID',
  `bizid` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '回执ID,可根据该ID查询具体的发送状态',
  `status` smallint(3) NOT NULL COMMENT '0=未发送，1=发送成功，2=发送失败，3=失效',
  `source` smallint(3) NOT NULL COMMENT '0=app，1=pc，2=其它',
  `invalid_time` timestamp NULL DEFAULT NULL COMMENT '失效时间',
  `modify_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='短信信息发送表';