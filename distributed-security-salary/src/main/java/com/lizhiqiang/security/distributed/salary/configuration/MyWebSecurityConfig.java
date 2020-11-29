package com.lizhiqiang.security.distributed.salary.configuration;

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 注入一个自定义的配置
 * @author Lizhiqiang
 * @date 2020/11/28 9:39
 */
//这里使用了@EnableGlobalMethodSecurity方法打开了基于注解的方法级别的权限验证
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class MyWebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()  //关闭csrf跨域检查
                .authorizeRequests()
                .antMatchers("/salary/**")
                // .hasAuthority("salary") //这里采用了注解的方法级权限配置。
                .authenticated() //其他请求需要登录
                .anyRequest().permitAll();
    }
}
