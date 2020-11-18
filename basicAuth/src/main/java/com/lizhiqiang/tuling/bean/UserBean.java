package com.lizhiqiang.tuling.bean;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 开始创建基于RBAC模型的三个关键实体
 * @author Lizhiqiang
 * @date 2020/11/16 16:59
 */
@Data
public class UserBean {

    private String userId;
    private String userName;
    private String userPass;
    private List<RoleBean> userRoles = new ArrayList<>();
    private List<ResourceBean> resourceBeans = new ArrayList<>();

    public UserBean(String userId, String userName, String userPass) {
        this.userId = userId;
        this.userName = userName;
        this.userPass = userPass;
    }

    public boolean havePermission(String resource) {
        return this.resourceBeans.stream().filter(resourceBean ->
                resourceBean.getResourceName().equals(resource)).count() > 0;
    }
}
