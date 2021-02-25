package com.lizhiqiang.security.distributed.salary.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 侯文远
 * @version V1.0
 * @Package com.lizhiqiang.security.distributed.uaa.controller
 * @date 2020/11/29 16:35
 * @Copyright © 李志强
 */
@RestController
@RequestMapping("salary")
public class SalaryController {

    //需要授权客户端拥有salary资源才可以访问 ,授权客户端必须添加这个资源ID，获得的token才能访问该接口resourceIds("order", "salary")
    @GetMapping("query")
    @PreAuthorize("hasAuthority('salary')")
    public String query(){
        return "salary info lizhiqiang 20k";
    }

}
