package com.controller;

import com.entity.ProductInfo;
import com.github.pagehelper.PageInfo;
import com.service.ProdInfoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/prod")
public class ProdInfoController {
    private final Integer PAGE_SIZE = 5;

    @DubboReference(interfaceClass = ProdInfoService.class)
    private ProdInfoService infoService;

    @RequestMapping("/splitPage")
    public String splitPage(Integer pageNum, Model model) {
        // 第一次进行分页查询时没有页码，默认为 0
        int num = pageNum == null ? 1 : pageNum;
        PageInfo<ProductInfo> list = infoService.splitPage(num, PAGE_SIZE);
        model.addAttribute("infoList",list);

        return "forward:/product.jsp";
    }
}
