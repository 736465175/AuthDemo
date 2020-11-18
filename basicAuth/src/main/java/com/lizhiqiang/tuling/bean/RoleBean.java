package com.lizhiqiang.tuling.bean;

import lombok.Data;

import java.util.List;

@Data
public class RoleBean {

    private String roleId;
    private String roleName;
    private List<ResourceBean> resources;

    public RoleBean(String roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }
}
