package com.conf;

import com.web.TokenCheck;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /*
        *  指定拦截的地址，除了与login相关的所有请求都进行拦截
        *  当出现404时，springboot会自动跳到、/error页面，
         * */
        String[] excludePath = {"/login","/loginPage","/favicon.ico","/","/error","/prod/splitPage"};
        registry.addInterceptor( new TokenCheck()).addPathPatterns("/prod/").excludePathPatterns(excludePath);
    }
}
