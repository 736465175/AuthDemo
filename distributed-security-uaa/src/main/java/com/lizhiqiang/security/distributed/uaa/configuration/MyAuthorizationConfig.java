package com.lizhiqiang.security.distributed.uaa.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.annotation.Resource;

//在使用OAuth2时，Spring Security也提供了一个类似的适配器来帮助我们完成配置
//这三个配置也是整个授权认证服务中最核心的配置
//AuthorizationServerConfigurerAdapter要求配置以下几个类，这几个类是由Spring创建的独立的配置
//        对象，它们会被Spring传入AuthorizationServerConfigurer中进行配置
/**
 * 总结
 * 1、ClientDetailsServiceConfigurer 配置客户端信息。
 * 2、AuthorizationServerEndpointsConfigurer 配置令牌服务。首选需要配置token如何存取，以及客
 * 户端支持哪些类型的token。然后不同的令牌服务需要不同的其他服务。authorization_code类型需要
 * 配置authorizationCodeServices来管理授权码，password类型需要UserDetailsService来验证用户身
 * 份。
 * 3、AuthorizationServerSecurityConfigurer 对相关endpoint定义一些安全约束
 *
 * @author Lizhiqiang
 * @date 2020/11/28 9:36
 */
@Configuration
public class MyAuthorizationConfig extends AuthorizationServerConfigurerAdapter {


    //用来配置令牌端点的安全约束
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.tokenKeyAccess("permitAll()") // oauth/token_key公开
                .checkTokenAccess("permitAll()") // oauth/check_token公开
                .allowFormAuthenticationForClients();  // 表单认证，申请令牌
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
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()   //内存配置的方式配置用户信息
            .withClient("c1") //clientId
            .secret(new BCryptPasswordEncoder().encode("secret")) //客户端秘钥
            .scopes("all") //客户端拥有的资源列表
            .authorizedGrantTypes("authorization_code", "password", "client_credentials", "implicit", "refresh_token") //客户端可以使用的授权类型,其实是代表了OAuth授权三方的不同互信程度
            .resourceIds("salary") //客户端拥有的资源列表
//            .resourceIds("order")
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
    /**
     *配置授权类型(Grant Types)
     * AuthorizationServerEndpointsConfigurer对于不同类型的授权类型，也需要配置不同的属性。
     * authenticationManager：认证管理器。当你选择了password(资源所有者密码)这个授权类型
     * 时，就需要指定authenticationManager对象来进行鉴权。
     * userDetailsService：用户主体管理服务。如果设置了这个属性，那说明有一个自己的
     * UserDetailsService接口的实现，或者你可以把这个东东设置到全局域(例如
     * GlobalAuthenticationManagerConfigurer)上去，当你设置了这个之后，那么refresh_token刷新
     * 令牌方式的授权类型流程中就会多包含一个检查步骤，来确保这个账号是否仍然有效。
     * authorizationCodeServices：这个属性是用来设置授权服务器的，主要用于 authorization_code
     * 授权码类型模式。
     * implicitGrantService：这个属性用于设置隐式授权模式的状态。
     * tokenGranter：如果设置了这个东东(即TokenGranter接口的实现类)，那么授权将会全部交由你
     * 来自己掌控，并且会忽略掉以上几个属性。这个属性一般是用作深度拓展用途的，即标准的四种授
     * 权模式已经满足不了你的需求时，才会考虑使用这个。
     *
     * 配置授权断点的URL(Endpoint URLS):
     * AuthorizationServerEndpointsConfifigurer这个配置对象首先可以通过pathMapping()方法来配置断
     * 点URL的链接地址。即将oauth默认的连接地址替代成其他的URL链接地址。例如spring security默认的
     * 授权同意页面/auth/confirm_access非常简陋，就可以通过passMapping()方法映射成自己定义的授权
     * 同意页面。
     *      框架默认的URL链接有如下几个：
     * /oauth/authorize ： 授权端点
     * /auth/token ： 令牌端点
     * /oauth/confirm_access ： 用户确认授权提交的端点
     * /oauth/error : 授权服务错误信息端点。
     * /oauth/check_token ： 用于资源服务访问的令牌进行解析的端点
     * /oauth/token_key ： 使用Jwt令牌需要用到的提供公有密钥的端点。
     * 需要注意的是，这几个授权端点应该被Spring Security保护起来只供授权用户访问
     *
     */
    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private TokenStore tokenStore;
    //会通过之前的ClientDetailsServiceConfigurer注入到Spring容器中
    @Autowired
    private ClientDetailsService clientDetailsService;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.pathMapping("/oauth/confirm_access","/customer/confirm_access")   //定制授权同意页面
        .authenticationManager(authenticationManager)   //认证管理器
        .userDetailsService(userDetailsService)     //密码模式的用户信息管理
        .authorizationCodeServices(authorizationCodeServices) //授权码服务
        .tokenServices(tokenService()) //令牌管理服务
        .allowedTokenEndpointRequestMethods(HttpMethod.POST);
    }

    //设置授权码模式的授权码如何存取，暂时用内存方式。
    @Bean
    public AuthorizationCodeServices getAuthorizationCodeServices(){
        return new InMemoryAuthorizationCodeServices();
        //JdbcAuthorizationCodeServices
    }

    @Resource
    private JwtAccessTokenConverter jwtAccessTokenConverter;


    public AuthorizationServerTokenServices tokenService() {
        DefaultTokenServices service = new DefaultTokenServices();
        service.setClientDetailsService(clientDetailsService); //客户端详情服务
        service.setSupportRefreshToken(true); //允许令牌自动刷新
//        service.setTokenStore(tokenStore); //令牌存储策略-内存
        //使用JWT令牌
        service.setTokenStore(tokenStore); //令牌存储策略-JwtTokenStore
        service.setTokenEnhancer(jwtAccessTokenConverter);
        service.setAccessTokenValiditySeconds(7200); // 令牌默认有效期2小时
        service.setRefreshTokenValiditySeconds(259200); // 刷新令牌默认有效期3天
        return service;
    }

}
