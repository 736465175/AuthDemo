package com.lizhiqiang.tuling.controller;

import com.lizhiqiang.tuling.bean.UserBean;
import com.lizhiqiang.tuling.service.AuthService;
import com.lizhiqiang.tuling.util.MyConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


//然后我们定义三个Controller，其中MobileController和SalaryController就是需要控制权限的访问资源，LoginController就是登陆的入口
@RestController
@RequestMapping("/common")
@Slf4j
public class LoginController {

    @Resource
    private AuthService authService;

    @PostMapping("/login")
    public UserBean login(UserBean loginUser, HttpServletRequest request){
        UserBean user = authService.userLogin(loginUser);
        if(user != null){
            log.info("user login succeed");
            request.getSession().setAttribute(MyConstants.FLAG_CURRENTUSER, user);
        }else{
            log.info("user login failed");
        }
        return user;
    }

    @PostMapping("/getCurrentUser")
    public Object getCurrentUser(HttpSession session){
        return session.getAttribute(MyConstants.FLAG_CURRENTUSER);
    }

    @PostMapping("/logout")
    public void logout(HttpSession session){
        session.removeAttribute(MyConstants.FLAG_CURRENTUSER);
    }

}
