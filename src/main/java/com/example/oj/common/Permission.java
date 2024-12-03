package com.example.oj.common;

import com.example.oj.domain.entity.UserInfo;

public enum Permission {

    ADMINISTRATOR(0L, "所有权限"),
    QUESTION_EDITOR(1L, "题目编辑者, 可以添加题目并编辑自己的题目和数据"),
    CONTEST_CONTROLLER(2L, "比赛编辑者, 可以组织并比赛, 修改自己的比赛"),
    USER_CONTROLLER(3L, "控制添加用户, 以及修改非自己的用户信息"),
    SOURCE_BROWSER(4L, "查看所有提交的代码, 以及代码查重权限"),
    ANNOUNCEMENT(5L, "公告添加"),
    DISCUSS_CONTROLLER(6L, "讨论板块控制, 可以删除别人发布的讨论");


    Long code;
    String permission;

    Permission(Long code, String permission) {
        this.code = code;
        this.permission = permission;
    }
    private Long getCode() {
        return this.code;
    }
   public static boolean CheckAuth(Permission per){
       Long code = per.getCode();
       UserInfo userInfo = BaseContext.getUserInfo();
       Long userType = userInfo.getUserType();
       if ((userType & 1L) != 0L) {
           return true;
       }
       if ((userType & (1L << code)) != 0) {
           return true;
       }
       return false;
   }
}
