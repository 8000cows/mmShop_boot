package com.utils;

import com.entity.ProductInfo;
import com.github.pagehelper.PageInfo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

public class RedisUtil {

    public static void deleteFromRedis(RedisTemplate<String, PageInfo<ProductInfo>> redisTemplate, Integer totalSize){

        // 设置redis的序列化方式
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        ValueOperations<String, PageInfo<ProductInfo>> valueOperations = redisTemplate.opsForValue();

        String key = "PAGE_";

        for(int i=1;i<=totalSize;i++){
            valueOperations.getAndDelete(key+i);
        }

    }
}
