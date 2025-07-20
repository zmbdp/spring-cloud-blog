package com.zmbdp.user.service;

import com.zmbdp.common.utils.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class RedisClientTest {

    @Autowired
    private RedisUtil redisUtil;

    @Test
    public void test() {
        /*// 测试查找元素
        System.out.println(redisUtil.hasKey("test"));
        System.out.println(redisUtil.getRedis("test"));
        // 测试插入元素
        System.out.println(redisUtil.setKey("test", "testValue"));
        System.out.println(redisUtil.hasKey("test"));
        System.out.println(redisUtil.getRedis("test"));

        // 插入一个 map 看看
        Map<String, Object> map = new HashMap<>();
        map.put("name", "zmbdp");
        map.put("age", 18);
        System.out.println(redisUtil.setKey("map", map));

        Map<String, Object> map2 = new HashMap<>();
        map2.put("name", "zmbdp111");
        map2.put("age", 18);
        System.out.println(redisUtil.setKey("map2", map2));*/


        System.out.println(redisUtil.setKey("captch", "111.com", 60 * 5));
        System.out.println(redisUtil.setKey("captch", "222.com", 60 * 5));
        System.out.println(redisUtil.setKey("captch", "333.com", 60 * 5));
    }
}
