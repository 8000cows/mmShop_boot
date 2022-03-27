package com.controller;

import com.entity.Admin;
import com.service.AdminService;
import com.utils.SecurityUtil;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class AdminController {
    @DubboReference(interfaceClass = AdminService.class)
    private AdminService service;

    @RequestMapping("/loginPage")
    public String toLoginPage(HttpServletRequest request){
        request.setAttribute("errMsg","");
        return "login";
    }

    @RequestMapping("/login")
    public String login(String name, String pwd, HttpServletRequest request) throws IOException {
        Admin admin = service.login(name,pwd);

        // 用户身份认证成功后，生成token令牌
        if(admin != null){
            request.setAttribute("admin",admin);
            request.setAttribute("token", SecurityUtil.createToken(admin));
            return "main";
        }

        request.setAttribute("errMsg","你输入的账号或密码不正确。");
        return "login";
    }
}
