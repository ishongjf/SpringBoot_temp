package com.hongjf.basics.entity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

/**
 * @Author: Hongjf
 * @Date: 2020/1/7
 * @Time: 11:32
 * @Description:拓展属性properties
 */
public class StringProperties extends HashMap<String, String> {
    public void setProperty(String key, String value) {
        put(key, value);
    }

    public String getProperty(String key) {
        return get(key);
    }

    public String getProperty(String key, String defaultValue) {
        String value = get(key);
        return value == null ? defaultValue : value;
    }

    public Set<String> propertyNames() {
        return Collections.unmodifiableSet(keySet());
    }

    @Override
    public int hashCode() {
        int h = 0;
        for (Entry<String, String> entry : entrySet()) {
            h += entry.hashCode();
        }
        return h;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (this == o) {
            return true;
        }
        StringProperties that = (StringProperties) o;
        if (size() != that.size()) {
            return false;
        }
        for (Entry<String, String> e : entrySet()) {
            if (!e.getValue().equals(that.get(e.getKey()))) {
                return false;
            }
        }
        return true;
    }
}
