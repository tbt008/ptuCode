package com.example.oj.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;

import lombok.Data;


@Data
public class UserRegisterDTO {

    /**
     * 用户名
     */
    @TableField("user_name")
    private String userName;

    /**
     * 昵称
     */
    @TableField("nick_name")
    private String nickName;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 用户个人简介（不得超过25字， 默认为该用户啥也没有写）
     */
    @TableField("description")
    private String description;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 手机号
     */
    @TableField("phone_number")
    private String phoneNumber;

    /**
     * 用户性别（0男，1女，2未知）
     */
    @TableField("sex")
    private String sex;

    /**
     * 头像
     */
    @TableField("avatar")
    private String avatar;

}
