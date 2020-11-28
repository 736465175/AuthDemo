package com.lizhiqiang.security.distributed.uaa.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * 注入一个自定义的配置
 * @author Lizhiqiang
 * @date 2020/11/28 9:39
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class MyWebSecurityConfig extends WebSecurityConfigurerAdapter {

//    @Autowired
//    private ClientDetailsService clientDetailsService;
//    @Autowired
//    private TokenStore tokenStore;

    @Bean
    public PasswordEncoder passwordEncoder() {
// return NoOpPasswordEncoder.getInstance();
        return new BCryptPasswordEncoder();
    }

    //从父类加载认证管理器
    @Bean
    public AuthenticationManager authenticationManagerBean() throws
            Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager(
                User.withUsername("admin").password(passwordEncoder().encode("admin")).authorities("mobile","salary").build(),
                User.withUsername("manager").password(passwordEncoder().encode("manager")).authorities("salary").build(),
                User.withUsername("worker").password(passwordEncoder().encode("worker")).authorities("worker").build()
        );
        return userDetailsManager;
    }

    //配置用户的安全拦截策略
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()  //关闭csrf跨域检查
                .authorizeRequests()
                .anyRequest().authenticated() //其他请求需要登录
                .and()
                .formLogin(); //可从默认的login页面登录，并且登录后跳转到main.html

    }
}
