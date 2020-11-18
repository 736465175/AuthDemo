package com.lizhiqiang.security.distributed.uaa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 2、注入AuthorizationServerTokenService
 在AuthorizationServer中定义AuthorizationServerTokenServices
 * @version V1.0
 * @Package com.lizhiqiang.security.distributed.uaa.service
 * @date 2020/11/18 22:26
 * @Copyright © 李志强
 */
public class AuthorizationServerTokenService {

    @Autowired
    private TokenStore tokenStore;
    //会通过之前的ClientDetailsServiceConfigurer注入到Spring容器中
    @Autowired
    private ClientDetailsService clientDetailsService;

    public AuthorizationServerTokenServices tokenService() {
        DefaultTokenServices service = new DefaultTokenServices();
        service.setClientDetailsService(clientDetailsService); //客户端详情服务
        service.setSupportRefreshToken(true); //允许令牌自动刷新
        service.setTokenStore(tokenStore); //令牌存储策略-内存
        service.setAccessTokenValiditySeconds(7200); // 令牌默认有效期2小时
        service.setRefreshTokenValiditySeconds(259200); // 刷新令牌默认有效期3天
        return service;
    }

}
