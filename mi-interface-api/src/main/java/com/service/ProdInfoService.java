package com.service;

import com.entity.ProductInfo;
import com.github.pagehelper.PageInfo;

public interface ProdInfoService {
    PageInfo<ProductInfo> splitPage(Integer pageNum, Integer pageSize);
}
