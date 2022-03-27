package com.service.impl;

import com.entity.ProductInfo;
import com.entity.ProductInfoExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mapper.ProductInfoMapper;
import com.service.ProdInfoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.OxmSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@DubboService(interfaceClass = ProdInfoService.class,timeout = 10000)
public class ProdInfoImpl implements ProdInfoService {
    @Resource
    private ProductInfoMapper infoMapper;
    @Resource
    private RedisTemplate<String,PageInfo<ProductInfo>> redisTemplate = new RedisTemplate<>();

    @Override
    public PageInfo<ProductInfo> splitPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        String key = "PAGE_"+pageNum.toString();  // 存在redis中的key
        List<ProductInfo> prodInfos ; // 将ProductInfo存在list中

        ProductInfoExample infoExample = new ProductInfoExample();
        infoExample.setOrderByClause("p_id desc");  // 根据主键倒序排，使最新的始终在前面

        // 设置redis的序列化方式
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        ValueOperations<String, PageInfo<ProductInfo>> valueOperations = redisTemplate.opsForValue();

        // 首先在redis里进行查询，检查是否有缓存
        PageInfo<ProductInfo> pageInfo = valueOperations.get(key);

        if(pageInfo == null){
            // 如果redis没有缓冲，再去数据库中进行查询,然后添加到redis
            prodInfos= infoMapper.selectByExample(infoExample);
            pageInfo = new PageInfo<>(prodInfos);
            valueOperations.set(key,pageInfo);
        }

        return pageInfo;
    }
}
