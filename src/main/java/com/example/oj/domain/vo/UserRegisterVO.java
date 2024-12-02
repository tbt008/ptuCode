package com.example.oj.domain.vo;

import lombok.Data;

@Data
public class UserRegisterVO {
    private String id;
    private String className;
    private String name;

    public UserRegisterVO(String id, String className, String name) {
        this.id = id;
        this.className = className;
        this.name = name;
    }
}
