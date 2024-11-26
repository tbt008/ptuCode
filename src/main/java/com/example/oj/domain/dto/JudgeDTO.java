package com.example.oj.domain.dto;

import lombok.Data;

@Data
public class JudgeDTO {

    /**
     * 编程语言
     */
    private Integer language;

    /**
     * 用户代码
     */
    private String code;


    /**
     * 题目 id
     */
    private Long questionId;

}
