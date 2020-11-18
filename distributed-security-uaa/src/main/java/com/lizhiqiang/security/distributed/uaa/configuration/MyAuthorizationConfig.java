package com.lizhiqiang.security.distributed.uaa.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

//在使用OAuth2时，Spring Security也提供了一个类似的适配器来帮助我们完成配置
//这三个配置也是整个授权认证服务中最核心的配置
//AuthorizationServerConfigurerAdapter要求配置以下几个类，这几个类是由Spring创建的独立的配置
//        对象，它们会被Spring传入AuthorizationServerConfigurer中进行配置
@Configuration
public class MyAuthorizationConfig extends AuthorizationServerConfigurerAdapter {


    //用来配置令牌端点的安全约束
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        super.configure(security);
    }


    /**用来配置客户端详情服务（ClientDetailsService），客户端详情
        信息在这里进行初始化，你能够把客户端详情信息写死在这里或者是通过数据库来存储调取详情信息,
     示例中我们暂时使用内存方式存储客户端详情信息
     ClientDetails中有几个重要的属性如下：
         clientId: 用来标识客户的ID。必须。
         secret: 客户端安全码，如果有的话。在微信登录中就是必须的。
         scope： 用来限制客户端的访问范围，如果是空(默认)的话，那么客户端拥有全部的访问范围。
         authrizedGrantTypes：此客户端可以使用的授权类型，默认为空。在微信登录中，只支持authorization_code这一种。
         authorities：此客户端可以使用的权限(基于Spring Security authorities)
         redirectUris：回调地址。授权服务会往该回调地址推送此客户端相关的信息。

     Client Details客户端详情，能够在应用程序运行的时候进行更新，可以通过访问底层的存储服务(例如
     访问mysql，就提供了JdbcClientDetailsService)或者通过自己实现ClientRegisterationService接口(同
     时也可以实现ClientDetailsService接口)来进行定制。

     示例中我们暂时使用内存方式存储客户端详情信息，配置如下
    */
    @Override  .....
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()   //内存配置的方式配置用户信息
            .withClient("c1") //clientId
            .secret(new BCryptPasswordEncoder().encode("secret")) //客户端秘钥
            .scopes("all") //客户端拥有的资源列表
            .authorizedGrantTypes("authorization_code", "password", "client_credentials", "implicit", "refresh_token") //客户端可以使用的授权类型
            .resourceIds("order") //客户端拥有的资源列表
            .autoApprove(false) //跳转到授权页面
            .redirectUris("https://www.baidu.com/"); //回调地址
            // .and() //继续注册其他客户端
            // .withClient()
            // ...
            // 加载自定义的客户端管理服务
            // clients.withClientDetails(clientDetailsService);
    }


    //用来配置令牌（token）的访问端点和令牌服务 (tokenservices)
    //AuthorizationServerEndpointsConfigurer这个对象的实例可以完成令牌服务以及令牌服务各个endpoint配置
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        super.configure(endpoints);
    }
}
