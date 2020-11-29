package com.lizhiqiang.security.distributed.salary.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

/**
 这里需要注意的是ResourceServerSecurityConfigurer的tokenServices()方法，设定了一个token的管
 理服务。其中，如果资源服务和授权服务是在同一个应用程序上，那可以使用DefaultTokenServices，
 这样的话，就不用考虑关于实现所有必要的接口一致性的问题。而如果资源服务器是分离的，那就必须
 要保证能够有匹配授权服务提供的ResourceServerTokenServices，他知道如何对令牌进行解码。
 令牌解析方法：使用DefaultTokenServices在资源服务器本地配置令牌存储、解码、解析方式。使用
 RemoteTokenServices资源服务器通过HTTP请求来解码令牌，每次都请求授权服务器端
 点/oauth/check_token。这时需要授权服务将这个端点暴露出来，以便资源服务进行访问。所以这里要
 注意下授权服务的下面这个配置：
     public void configure(AuthorizationServerSecurityConfigurer security) throws
     Exception {
         security.tokenKeyAccess("permitAll()")// /oauth/token_key 允许访问
         .checkTokenAccess("permitAll()") // /oauth/check_token 允许访问
     }
 而这个/oauth/check_token端点可以获取到access_token对应到的客户信息
 * @date 2020/11/29 16:04
 * @Copyright © 李志强
 */
@Configuration
public class MyResourceServerConfig extends ResourceServerConfigurerAdapter {

    private static final String RESOURCE_SALARY = "salary";

    //配置access_token远程验证策略
    public ResourceServerTokenServices tokenServices(){
        RemoteTokenServices services = new RemoteTokenServices();
        services.setCheckTokenEndpointUrl("http://localhost:53020/uaa/oauth/check_token");
        services.setClientId("c1");
        services.setClientSecret("secret");
        return services;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(RESOURCE_SALARY)  //资源ID
                 .tokenServices(tokenServices()) //使用远程服务验证令牌的服务
                 .stateless(true); //无状态模式
    }

    //配置安全策略
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests() //校验请求
            .antMatchers("/order/**") // 路径匹配规则
            .access("#oauth2.hasScope('all')")  // 需要匹配scope
            .and().csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);  //???????
    }
}
