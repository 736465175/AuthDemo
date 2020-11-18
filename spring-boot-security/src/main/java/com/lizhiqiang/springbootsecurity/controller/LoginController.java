package com.lizhiqiang.springbootsecurity.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;


//然后我们定义三个Controller，其中MobileController和SalaryController就是需要控制权限的访问资源，LoginController就是登陆的入口
@RestController
@RequestMapping("/common")
@Slf4j
public class LoginController {

    @GetMapping("/getLoginUserByPrincipal")
    public String getLoginUserByPrincipal(Principal principal){
        return principal.getName();
    }
    @GetMapping(value = "/getLoginUserByAuthentication")
    public String currentUserName(Authentication authentication) {
        return authentication.getName();
    }
    @GetMapping(value = "/username")
    public String currentUserNameSimple(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        return principal.getName();
    }
    @GetMapping("/getLoginUser")
    public String getLoginUser(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getUsername();
    }

}
