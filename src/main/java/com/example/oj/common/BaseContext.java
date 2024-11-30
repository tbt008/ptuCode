package com.example.oj.common;

import com.example.oj.domain.entity.UserInfo;

public class BaseContext {
    private static ThreadLocal<UserInfo> threadLocal = new ThreadLocal<>();


    public static UserInfo getUserInfo() {
       return threadLocal.get();
    }
    public static void setUserInfo(UserInfo userInfo) {
        threadLocal.set(userInfo);
    }
    public static void removeUserInfo() {
        threadLocal.remove();
    }
}
