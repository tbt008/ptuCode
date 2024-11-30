package com.example.oj.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;

import lombok.Data;


@Data
public class UserRegisterDTO {

    /**
     * 账号
     */
    private String id;
    /**
     * 密码
     */
    private String password;





}
