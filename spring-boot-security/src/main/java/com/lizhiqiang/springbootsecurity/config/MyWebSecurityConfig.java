package com.lizhiqiang.springbootsecurity.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 注入一个自定义的配置
 */
@EnableWebSecurity
public class MyWebSecurityConfig extends WebSecurityConfigurerAdapter {
    //配置安全拦截策略
    @Override
    protected void configure(HttpSecurity http) throws Exception {
//链式配置拦截策略
        http.csrf().disable()//关闭csrg跨域检查
                .authorizeRequests()
                .antMatchers("/mobile/**").hasAuthority("mobile") //配置资源权限
                .antMatchers("/salary/**").hasAuthority("salary")
                .antMatchers("/common/**").permitAll() //common下的请求直接通过
                .anyRequest().authenticated() //其他请求需要登录
                .and() //并行条件
                .formLogin().defaultSuccessUrl("/main.html").failureUrl("/common/loginFailed"); //可从默认的login页面登录，并且登录后跳转到main.html
    }
}
