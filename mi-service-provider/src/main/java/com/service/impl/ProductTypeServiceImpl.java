package com.service.impl;

import com.entity.ProductType;
import com.entity.ProductTypeExample;
import com.mapper.ProductTypeMapper;
import com.service.ProductTypeService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.util.List;

@DubboService(interfaceClass = ProductTypeService.class)
public class ProductTypeServiceImpl implements ProductTypeService {

    @Resource
    private ProductTypeMapper typeMapper;

    @Override
    public List<ProductType> getAll() {
        return typeMapper.selectByExample(new ProductTypeExample());
    }
}
