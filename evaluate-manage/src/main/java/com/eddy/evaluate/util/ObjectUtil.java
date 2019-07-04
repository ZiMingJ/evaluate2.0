package com.eddy.evaluate.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import java.math.BigDecimal;
import java.util.Map;

public class ObjectUtil {

    /**
     * 对象序列化为Map
     *
     * @param obj
     * @return
     */
    public static CustomMap convertObjectToMap(Object obj) {
        String json = JSONObject.toJSONString(obj, SerializerFeature.WriteNullStringAsEmpty);
        return JSONObject.parseObject(json, CustomMap.class);
    }


    /**
     * 转换为  BigDecimal
     *
     * @param str
     * @return
     */
    public static BigDecimal convertBigDecimal(Object str) {
        return new BigDecimal(String.valueOf(str));
    }


    /**
     * 将Map字段Key 下划线转化为驼峰命名
     *
     * @param params
     * @return
     */
    public static CustomMap fieldCaseToCamel(Map<String, Object> params) {
        CustomMap customMap = CustomMap.create();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();
            if (key.contains("_")) {
                key = StringUtils.underlineToCamel(key);
            }
            customMap.put(key, entry.getValue());
        }
        return customMap;
    }

    /**
     * 判断字段不为空 如果其中一个为空返回 false
     *
     * @param objs
     * @return
     */
    public static boolean checkParamsNotNull(Object... objs) {
        for (Object o : objs) {
            if (o instanceof Integer) {
                if (o == null) {
                    return false;
                }
            } else if (o instanceof String) {
                if (org.apache.commons.lang3.StringUtils.isBlank((String) o)) {
                    return false;
                }
            } else if (o instanceof Map) {
                if (!FieldCheckUtil.isMapFieldNotNull((Map) o)) {
                    return false;
                }
            } else {
                if (o == null) {
                    return false;
                }
            }
        }
        return true;
    }

}
