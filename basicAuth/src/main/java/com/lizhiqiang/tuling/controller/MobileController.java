package com.lizhiqiang.tuling.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


//然后我们定义三个Controller，其中MobileController和SalaryController就是需要控制权限的访问资源，LoginController就是登陆的入口
@RestController
@RequestMapping("/mobile")
public class MobileController {

    @GetMapping("/query")
    public String query() {
        return "mobile";
    }

}
