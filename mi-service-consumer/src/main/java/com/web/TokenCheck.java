package com.web;

import com.utils.SecurityUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 对token进行检查
public class TokenCheck implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //System.out.println("request Uri : "+request.getRequestURI());
        int result = SecurityUtil.parseToken(request);

        if(result == 1){
            request.setAttribute("errMsg","Token有效时间已过，请重新登录!");
            return false;
        }else if (result == 2) {
            return true;
        }

        System.out.println(result);
        request.setAttribute("errMsg","Token信息有误!");
        return false;
    }
}
