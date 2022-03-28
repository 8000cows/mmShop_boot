package com.advice;

import com.entity.ProductInfo;
import com.entity.ProductInfoExample;
import com.github.pagehelper.PageInfo;
import com.mapper.ProductInfoMapper;
import com.utils.RedisUtil;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

@Aspect
public class CacheAdvice {

    @Resource
    private ProductInfoMapper infoMapper;
    @Resource
    private RedisTemplate<String, PageInfo<ProductInfo>> redisTemplate = new RedisTemplate<>();

    @Before(value = "execution(* com.service.impl.*Prod(..))")
    public void removeCache(){
        int count = infoMapper.countByExample(new ProductInfoExample());
        System.out.println("count : "+count);
        /*  增删改操作会导致redis中数据与数据不一致,
            因此每次增删改操作都要先将redis中缓冲清除   */

        int totalSize = count % 5 == 0 ? count/5 : count/5+1;
        RedisUtil.deleteFromRedis(redisTemplate,totalSize);
    }
}
