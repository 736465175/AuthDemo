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

    @GetMapping("query")
    @PreAuthorize("hasAuthority('salary')")  //需要授权客户端拥有order资源才可以访问
    public String query(){
        return "salary info lizhiqiang 20k";
    }

}
