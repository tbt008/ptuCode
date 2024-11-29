package com.example.oj.common;

import lombok.Data;

@Data
public class CodeResponse {
    private Object err;  // 返回信息
    private String data;
}
