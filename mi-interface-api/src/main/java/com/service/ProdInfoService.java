package com.service;

import com.entity.Condition;
import com.entity.ProductInfo;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ProdInfoService {
    PageInfo<ProductInfo> splitPage(Integer pageNum, Integer pageSize);

    int addProd(ProductInfo info);

    ProductInfo queryById(Integer pid);

    int updateProd(ProductInfo info);

    int deleteProd(Integer pId);

    int deleteBatchProd(String[] ids);

    List<ProductInfo> selectByCondition(Condition condition);

    PageInfo<ProductInfo> splitPageByCondition(Condition condition,Integer pageNum);
}
