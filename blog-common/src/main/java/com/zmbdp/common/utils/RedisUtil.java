package com.zmbdp.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

@Slf4j
public class RedisUtil {

    private StringRedisTemplate redisTemplate;

    public RedisUtil(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /*=============================================    String    =============================================*/

    /**
     * 获取 redis 的 key 的数据
     *
     * @param key 键
     * @return 值
     */
    public String getRedis(String key) {
        if (!StringUtils.hasText(key)) {
            return null;
        }
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error("RedisUtil.getRedis error: key: {}; e: {}", key, e.getMessage());
            return null;
        }
    }

    /**
     * 判断 redis 的 key 是否存在
     *
     * @param key 键
     * @return 存在返回 true, 不存在返回 false
     */
    public boolean hasKey(String key) {
        if (!StringUtils.hasText(key)) {
            return false;
        }
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error("RedisUtil.hasKey error: key: {}; e: {}", key, e.getMessage());
            return false;
        }
    }

    /**
     * 插入无过期时间的 redis 的 key
     *
     * @param key   键
     * @param value 值
     * @return true: 成功 ; false: 失败
     */
    public boolean setKey(String key, String value) {
        if (key == null || value == null) {
            return false;
        }
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error("RedisUtil.setKey error: key: {}; value: {}; e: {}", key, value, e.getMessage());
            return false;
        }
    }

    /**
     * 插入有过期时间的 redis 的 key
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return 插入成功返回 true, 插入失败返回 false
     */
    public boolean setKey(String key, String value, long time) {
        if (!StringUtils.hasText(key) || value == null) {
            return false;
        }
        try {
            if (time <= 0) {
                return setKey(key, value);
            }
            // 设置 key-value 并且设置有效期
            redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            // 处理其他异常
            log.error("Redis.setKey error: key: {}; value: {}; e: {}", key, value, e.getMessage());
            return false;
        }
    }


    /**
     * 删除 redis 的 key
     *
     * @param key 键
     * @return 删除成功返回 true
     */
    public boolean deleteKey(String key) {
        if (!StringUtils.hasText(key)) {
            return true;
        }
        try {
            return redisTemplate.delete(key);
        } catch (Exception e) {
            log.error("RedisUtil.deleteKey error: key: {}; e: {}", key, e.getMessage());
            return false;
        }
    }

    /**
     * 获取 Redis Key 的剩余过期时间（秒）
     *
     * @param key Redis 键
     * @return 剩余时间（秒）
     * -1 表示永不过期
     * -2 表示 key 不存在
     */
    public long getExpireTime(String key) {
        if (!StringUtils.hasText(key)) {
            log.warn("Redis key 为空");
            return -2;
        }
        try {
            Long expire = redisTemplate.getExpire(key);
            if (expire == null) {
                return -2;
            }
            return expire;
        } catch (Exception e) {
            log.error("RedisUtil.getExpireTime error: key: {}; e: {}", key, e.getMessage());
            return -2;
        }
    }

    /**
     * 对 key 的值进行原子递增（+1）
     * @param key Redis 键
     * @return 递增后的值（失败返回 -1）
     */
    public long incr(String key) {
        if (!StringUtils.hasText(key)) {
            return -1;
        }
        try {
            return redisTemplate.opsForValue().increment(key);
        } catch (Exception e) {
            log.error("RedisUtil.incr error: key={}", key, e);
            return -1;
        }
    }

    /**
     * 对 key 的值进行原子递减（-1）
     * @param key Redis 键
     * @return 递减后的值（失败返回 -1）
     */
    public long decr(String key) {
        try {
            return redisTemplate.opsForValue().decrement(key);
        } catch (Exception e) {
            log.error("Redis decr error: {}", key, e);
            return -1;
        }
    }

    /**
     * 设置超时时间
     * @param key 键
     * @param seconds 时间(秒)
     */
    public void expire(String key, long seconds) {
        try {
            redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Redis expire error: {}", key, e);
        }
    }


    /*=============================================    Hash    =============================================*/

    /**
     * 存储 Hash 结构数据
     *
     * @param key      Redis键
     * @param mapKey   Hash字段（如手机号）
     * @param mapValue 字段值（如验证码）
     * @return 存储成功返回 true, 存储失败返回 false
     */
    public boolean setHash(String key, String mapKey, String mapValue) {
        if (!StringUtils.hasText(key) || !StringUtils.hasText(mapKey)) {
            return false;
        }
        try {
            redisTemplate.opsForHash().put(key, mapKey, mapValue);
            return true;
        } catch (Exception e) {
            log.error("RedisUtil.setHash error: key={}, field={}", key, mapKey, e);
            return false;
        }
    }

    /**
     * 存储 Hash 结构数据并设置过期时间
     *
     * @param key      Redis键
     * @param mapKey   Hash字段
     * @param mapValue 字段值
     * @param timeout  过期时间
     * @param unit     时间单位
     * @return true=成功, false=失败
     */
    public boolean setHash(String key, String mapKey, String mapValue, long timeout, TimeUnit unit) {
        if (!StringUtils.hasText(key) || !StringUtils.hasText(mapKey)) {
            return false;
        }
        try {
            // 1. 存储Hash数据
            redisTemplate.opsForHash().put(key, mapKey, mapValue);

            // 2. 设置过期时间（仅在timeout>0时生效）
            if (timeout > 0) {
                redisTemplate.expire(key, timeout, unit);
            }
            return true;
        } catch (Exception e) {
            log.error("RedisUtil.setHash error: key={}, field={}", key, mapKey, e);
            return false;
        }
    }

    /**
     * 获取 Redis HashMap 中指定字段的值
     *
     * @param key    Redis键
     * @param mapKey Hash字段
     * @return 字段值（不存在返回 null）
     */
    public String getHash(String key, String mapKey) {
        if (!StringUtils.hasText(key) || !StringUtils.hasText(mapKey)) {
            return null;
        }
        try {
            return (String) redisTemplate.opsForHash().get(key, mapKey);
        } catch (Exception e) {
            log.error("RedisUtil.getHash error: mapKey={}, mapValue={}", key, mapKey, e);
            return null;
        }
    }

    /**
     * 删除 Hash 结构中的字段
     *
     * @param key    Redis键
     * @param mapKey Hash字段
     * @return 删除成功返回 true, 删除失败返回 false
     */
    public boolean deleteHash(String key, String mapKey) {
        if (!StringUtils.hasText(key) || !StringUtils.hasText(mapKey)) {
            return false;
        }
        try {
            redisTemplate.opsForHash().delete(key, mapKey);
            return true;
        } catch (Exception e) {
            log.error("RedisUtil.deleteHash error: key={}, mapKey={}", key, mapKey, e);
            return false;
        }
    }



    /**
     * 带过期时间的原子递增（首次递增时设置过期时间）
     * @param key Redis 键
     * @param expireSeconds 过期时间（秒）
     * @return 递增后的值（失败返回 -1）
     */
    public long incr(String key, long expireSeconds) {
        if (!StringUtils.hasText(key)) {
            return -1;
        }
        try {
            Long newValue = redisTemplate.opsForValue().increment(key);
            if (newValue != null && newValue == 1 && expireSeconds > 0) {
                // 只有第一次递增时设置过期时间
                redisTemplate.expire(key, expireSeconds, TimeUnit.SECONDS);
            }
            return newValue != null ? newValue : -1;
        } catch (Exception e) {
            log.error("RedisUtil.incrWithExpire error: key={}", key, e);
            return -1;
        }
    }

}
