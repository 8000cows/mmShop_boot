package com.controller;

import com.entity.Condition;
import com.entity.ProductInfo;
import com.entity.ProductType;
import com.github.pagehelper.PageInfo;
import com.service.ProdInfoService;
import com.service.ProductTypeService;
import com.utils.FileNameUtil;
import org.apache.dubbo.config.annotation.DubboReference;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/prod")
public class ProdInfoController {
    private final Integer PAGE_SIZE = 5;
    private String fileName = "";

    @DubboReference(interfaceClass = ProdInfoService.class)
    private ProdInfoService infoService;
    @DubboReference(interfaceClass = ProductTypeService.class)
    private ProductTypeService typeService;

    @RequestMapping("/splitPage")
    public String splitPage(Integer pageNum, HttpServletRequest request) {
        // 第一次进行分页查询时没有页码，默认为 0
        int num = pageNum == null ? 1 : pageNum;
        //PageInfo<ProductInfo> list = infoService.splitPage(num, PAGE_SIZE);
        //request.setAttribute("infoList",list);

        List<ProductType> types = typeService.getAll();
        request.getServletContext().setAttribute("typeList",types);

        Condition  condition = (Condition) request.getSession().getAttribute("condition");
        if(condition!=null){
            request.setAttribute("infoList",infoService.splitPageByCondition(condition,condition.getPageNum()));
            request.setAttribute("condition",condition);
            request.getSession().removeAttribute("condition");
        }else{
            request.setAttribute("infoList",infoService.splitPage(num,PAGE_SIZE));
        }

        return "forward:/product.jsp";
    }

    // ajax 获取商品信息，分页
    // 多条件查询分页
    @RequestMapping(value = "/ajaxSplit.action")
    @ResponseBody
    public void ajaxSplit(Condition condition, HttpSession session){
        //public void ajaxSplit(Integer pageNum, HttpSession session){
        //int num = pageNum==null ? 0 : pageNum;
        //session.setAttribute("infoList",infoService.splitPage(num,PAGE_SIZE));

        session.setAttribute("infoList",infoService.splitPageByCondition(condition,condition.getPageNum()));
    }

    // 多条件查询，不分页
    @RequestMapping(value = "/condition.action")
    @ResponseBody
    public void queryByCondition(Condition condition,HttpSession session){
        List<ProductInfo> infoList = infoService.selectByCondition(condition);
        PageInfo<ProductInfo> pageInfo = new PageInfo<>(infoList);

        session.setAttribute("infoList",pageInfo);
    }

    // ajax 上传图片
    @RequestMapping(value = "/ajaxImg.action")
    @ResponseBody
    public Object ajaxImg(MultipartFile image, HttpServletRequest request){

        fileName = FileNameUtil.getUUIDFileName() + FileNameUtil.getFileType(image.getOriginalFilename());
        //String path = request.getServletContext().getRealPath("/image_big");
        String path = "E:\\Java练习\\IDEA-Practice\\ssm-project\\mi-springboot\\mi-service-consumer\\src\\main\\resources\\static\\image_big";

        // 存储
        try {
            String imgPath = path + File.separator + fileName;
            image.transferTo(new File(imgPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject obj = new JSONObject();
        obj.append("imgUrl",fileName);

        return obj.toString();
    }

    // 添加商品
    @RequestMapping(value = "/add.action")
    public String addProd(ProductInfo info,HttpServletRequest request){
        info.setpImage(fileName);
        info.setpDate(new Date());

        int result;
        result = infoService.addProd(info);

        if (result == 1)
            request.setAttribute("msg","添加商品成功");
        else
            request.setAttribute("msg","添加商品失败");

        fileName = "";

        return "forward:/prod/splitPage";
    }

    // 跳转到更新商品页面
    @RequestMapping(value = "/queryById.action")
    public String updatePage(Integer pid,HttpServletRequest request,Condition condition){

        ProductInfo info =  infoService.queryById(pid);
        request.setAttribute("prod",info);
        request.getSession().setAttribute("condition",condition);
        return "forward:/update.jsp";
        //return "forward:/admin/update.jsp?page="+page;
    }

    // 更新商品
    @RequestMapping(value = "/update.action")
    public String updateProd(ProductInfo info,HttpServletRequest request){

        if(!fileName.equals("")){
            info.setpImage(fileName);
        }

        int res = infoService.updateProd(info);

        if(res == 1)
            request.setAttribute("msg","更新成功!");
        else
            request.setAttribute("msg","更新失败!");

        return "forward:/prod/splitPage";
    }

    // 删除商品
    @RequestMapping(value = "/delete.action")
    public String deleteProd(Integer pid,Condition condition,HttpServletRequest request){
        int res = infoService.deleteProd(pid);

        if(res == 1){
            request.setAttribute("msg", "删除成功！");
            request.getSession().setAttribute("condition",condition);
        }
        else
            request.setAttribute("msg","删除失败！");

        return "forward:/prod/ajaxDelete.action?pageNum="+condition.getPageNum();
    }

    @RequestMapping(value = "/ajaxDelete.action",produces = "text/html;charset=utf-8")
    @ResponseBody
    public Object ajaxDelete(HttpServletRequest request,Integer pageNum){
        Condition condition = (Condition) request.getSession().getAttribute("condition");
        System.out.println(condition);

        if (condition!=null){
            request.getSession().setAttribute("infoList",infoService.splitPageByCondition(condition,pageNum));
            request.getSession().removeAttribute("condition");
        }else{
            request.getSession().setAttribute("infoList",infoService.splitPage(1,PAGE_SIZE));
        }
        return request.getAttribute("msg");
    }

    // 批量删除
    @RequestMapping(value = "/deleteBatch.action")
    public String deleteBatch(String pIds,Condition condition,HttpServletRequest request){

        String[] pid = pIds.split(",");
        int res = infoService.deleteBatchProd(pid);

        if(res == pid.length){
            request.setAttribute("msg", "删除成功！");
            request.getSession().setAttribute("condition",condition);
        }else{
            request.setAttribute("msg","批量删除"+res+"条商品失败！");
        }

        return "forward:/prod/ajaxDelete.action?pageNum="+condition.getPageNum();
    }
}
