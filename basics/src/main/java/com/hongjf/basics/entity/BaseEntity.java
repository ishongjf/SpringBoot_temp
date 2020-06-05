package com.hongjf.basics.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hongjf.common.utils.ToolUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Hongjf
 * @Date: 2020/1/3
 * @Time: 17:25
 * @Description:基础实体类
 */
@Data
@Slf4j
public class BaseEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Timestamp createTime;

    private Timestamp updateTime;

    private StringProperties properties = new StringProperties();

    private Long createUserId;

    private Long updateUserId;

    public String getProperties() {
        return JSONObject.toJSONString(properties);
    }

    public void setProperties(String json) {
        if (ToolUtil.isNotEmpty(json)) {
            try {
                Map<String, String> map = JSON.parseObject(json, new TypeReference<Map<String, String>>() {
                });
                map.forEach((k, v) -> {
                    properties.setProperty(k, v);
                });
            } catch (Exception e) {
                log.info("fail to parse properties, json[{}]", json, e);
            }
        }
    }

    @JsonIgnore
    public Map<String, String> getPropertiesMap() {
        Map<String, String> map = new HashMap<>();
        if (properties != null) {
            for (String key : properties.keySet()) {
                map.put(key, properties.get(key));
            }
        }
        return map;
    }

    public void setProperties(Map<String, String> map) {
        if (map != null) {
            for (String key : map.keySet()) {
                properties.setProperty(key, map.get(key));
            }
        }
    }

    public String getProperty(String key) {
        if (ToolUtil.isEmpty(properties.get(key))) {
            return "";
        }
        return properties.get(key);
    }

    public void setProperty(String key, String value) {
        properties.put(key, value);
    }

    public void delProperty(String key) {
        if (properties.containsKey(key)) {
            properties.remove(key);
        }
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
