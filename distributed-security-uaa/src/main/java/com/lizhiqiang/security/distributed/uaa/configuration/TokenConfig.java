package com.lizhiqiang.security.distributed.uaa.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

/**
 管理令牌
 AuthorizationServerTokenService接口定义了一些对令牌进行管理的必要操作，令牌可以被用来加载
 身份信息，里面包含了这个令牌的相关权限。
 实现一个AuthorizationServerTokenServices这个接口，需要继承DefaultTokenServices这个类。 该类
 中包含了一些有用的实现。你可以使用它来修改令牌的格式和令牌的存储。默认情况下，他在创建一个
 令牌时，是使用随机值来进行填充的。这个类中完成了令牌管理的几乎所有的事情，唯一需要依赖的是
 spring容器中的一个TokenStore接口实现类来定制令牌持久化。而这个TokenStore，有一个默认的实
 现，就是ImMemoryTokenStore，这个类会将令牌保存到内存中。除此之外，还有几个默认的
 TokenStore实现类可以使用。
 InMemoryTokenStore：这个是默认采用的方式。他可以在单服务器上完美运行(即并发访问压力
 不大的情况下，并且他在失败时不会进行备份)。大多数的项目都可以使用这个实现类来进行尝
 试。也可以在并发的时候来进行管理，因为不会被保存到磁盘中，所以更易于调试。
 JdbcTokenStore：这是一个基于JDBC的实现类，令牌会被保存到关系型数据库中。使用这个实现
 类，可以在不同的服务器之间共享令牌信息。当然，这个是需要使用spring boot jdbc相关的依赖
 的。类似的，还有RedisTokenStore基于Redis存储令牌信息。
 JwtTokenStore：全程是JSON Web Token。他可以把令牌信息全部编码整合进令牌本身，这样后
 端服务可以不用存储令牌相关信息，这是他最大的优势。但是他也有缺点， 那就是撤销一个已经
 授权的令牌会非常困难。所以他通常用来处理一个生命周期较短的令牌以及撤销刷新令牌
 (refresh_token)。而另一个缺点就是这个令牌会比较大，因为他要包含较多的用户凭证信息。
 JwtTokenStore不会保存任何数据，但是他在转换令牌值以及授权信息方面和
 DefaultTokenServices所扮演的角色是一样的。
 所以我们下面的步骤首先是要定义一个TokenStore
 1、注入TokenConfig
 我们先定义一个TokenConfig，往Spring容器中注入一个InMemoryTokenStore，生成一个普通令牌
 * @date 2020/11/18 22:20
 * @Copyright © 李志强
 */
@Configuration
public class TokenConfig {
    @Bean
    public TokenStore tokenStore(){
        //使用基于内存的普通令牌
        return new InMemoryTokenStore();
    }
}
