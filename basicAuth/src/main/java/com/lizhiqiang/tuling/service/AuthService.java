package com.lizhiqiang.tuling.service;

import com.lizhiqiang.tuling.bean.UserBean;
import com.lizhiqiang.tuling.util.TestData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

//LoginController中依赖AuthService，来对登陆进行认证
@Service
public class AuthService {

    private final String demoUserName = "admin";
    private final String demoUserPass = "admin";

    @Resource
    private TestData testData;

    public UserBean userLogin(UserBean loginUser) {
        UserBean queryUser = testData.queryUser(loginUser);
        if(queryUser != null){
            queryUser.setUserId(UUID.randomUUID().toString());
        }
        return queryUser;
    }
}
