package com.lizhiqiang.springbootsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *
 目前示例中的权限规则都是从内存直接写死的，实际项目中显然都是要从数据库进行加载。而且，
 目前我们的规则都是基于web请求路径来定制的，而Spring Security实际上还提供了基于注解的方法级
 别规则配置
 */
@Configuration
public class MyWebConfig implements WebMvcConfigurer {
    //默认Url根路径跳转到/login，此url为spring security提供
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("redirect:/login");
    }
    /**
     * 自行注入一个PasswordEncoder。
     * @return
     */
    @Bean
    public PasswordEncoder getPassWordEncoder(){
        return new BCryptPasswordEncoder(10);
// return NoOpPasswordEncoder.getInstance();
    }
    /**
     * 自行注入一个UserDetailsService
     * 如果没有的话，在UserDetailsServiceAutoConfiguration中会默认注入一个包含
     user用户的InMemoryUserDetailsManager
     * 另外也可以采用修改configure(AuthenticationManagerBuilder auth)方法并注入
     authenticationManagerBean的方式。
     * @return
     */
    @Bean
    public UserDetailsService userDetailsService(){
        InMemoryUserDetailsManager userDetailsManager = new
                InMemoryUserDetailsManager(User.withUsername("admin").password(getPassWordEncoder().encode("admin")).authorities("mobile","salary").build(),
                User.withUsername("manager").password(getPassWordEncoder().encode("manager")).authorities("salary").build(),
                User.withUsername("worker").password(getPassWordEncoder().encode("worker")).authorities("worker").build());
        return userDetailsManager;
// return new JdbcUserDetailsManager(DataSource dataSource);
    }
}