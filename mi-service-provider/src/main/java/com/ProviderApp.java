package com;

import com.github.pagehelper.PageHelper;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Properties;

@SpringBootApplication
@EnableDubbo
@MapperScan(basePackages = "com.mapper")
public class ProviderApp {

    public static void main(String[] args) {
        SpringApplication.run(ProviderApp.class, args);
    }

    @Bean
    public PageHelper pageHelper() {
        PageHelper pageHelper = new PageHelper();
        Properties p = new Properties();
        p.setProperty("helperDialect", "Mysql");
        p.setProperty("supportMethodsArguments", "true");
        p.setProperty("reasonable", "false");
        p.setProperty("params", "count=countSql");
        pageHelper.setProperties(p);
        return pageHelper;
    }

}

