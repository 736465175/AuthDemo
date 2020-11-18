package com.lizhiqiang.tuling.interceptor;

import com.lizhiqiang.tuling.bean.UserBean;
import com.lizhiqiang.tuling.util.MyConstants;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//这个AuthInterceptor，就是以拦截器的形式来实现权限管控
@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1、不需要登录就可以访问的路径
        String requestURI = request.getRequestURI();
        if(requestURI.contains(".") || requestURI.startsWith("/" + MyConstants.RESOURCE_COMMON + "/")){
            return true;
        }

        //2、未登录用户，直接拒绝访问
        if(null == request.getSession().getAttribute(MyConstants.FLAG_CURRENTUSER)){
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("please login first");
            return false;
        }else{
            //3、已登录用户，判断是否有资源访问权限
            UserBean currentUser = (UserBean) request.getSession().getAttribute(MyConstants.FLAG_CURRENTUSER);
            if(requestURI.startsWith("/" + MyConstants.RESOURCE_MOBILE + "/") &&
                    currentUser.havePermission(MyConstants.RESOURCE_MOBILE)){
                return true;
            }else if(requestURI.startsWith("/" + MyConstants.RESOURCE_SALARY + "/") &&
                    currentUser.havePermission(MyConstants.RESOURCE_SALARY)){
                return true;
            }else {
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("no auth to visit");
                return false;
            }

        }





























    }
}
