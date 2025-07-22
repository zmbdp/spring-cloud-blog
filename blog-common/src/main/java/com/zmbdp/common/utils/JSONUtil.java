package com.zmbdp.common.utils;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Slf4j
public class JSONUtil<T> {
    /**
     * 将对象转为JSON字符串
     *
     * @param obj 对象
     * @return JSON字符串
     */
    public static String toJson(Object obj) {
        try {
            return JSON.toJSONString(obj);
        } catch (Exception e) {
            log.error("JSONUtil.toJson error, e: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 将JSON字符串转为对象
     *
     * @param json  JSON字符串
     * @param clazz 对象类型
     * @return 对象
     */
    public static <T> T toClass(String json, Class<T> clazz) {

        if (!StringUtils.hasLength(json) || clazz == null) {
            return null;
        }

        try {
            return JSON.parseObject(json, clazz);
        } catch (Exception e) {
            log.error("JSONUtil.parseObject error, json: {}, clazz: {}, e: {}", json, clazz.getName(), e.getMessage());
            return null;
        }
    }
}
