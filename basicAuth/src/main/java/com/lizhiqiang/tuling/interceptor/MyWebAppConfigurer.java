package com.lizhiqiang.tuling.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

//们注入一个配置器WebMvcConfigurer，来对SpringBoot进行部分配置
@Component
public class MyWebAppConfigurer implements WebMvcConfigurer {

    @Resource
    private AuthInterceptor authInterceptor;

    //配置权限拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor).addPathPatterns("/**");
    }

    //简单配置启动页面
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("redirect:/index.html");
    }
}
