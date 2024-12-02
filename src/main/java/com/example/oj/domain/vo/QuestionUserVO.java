package com.example.oj.domain.vo;


import lombok.Data;

@Data
public class QuestionUserVO {
    /**
     * 题目状态 0 未提交 1 通过 2尝试过
     */

    private Integer isPass;

    /**
     * 题目id
     */
    private Integer titleId;

    /**
     * 标题
     */
    private String titleName;

    /**
     * 难度
     */
    private Integer score;

    /**
     * 通过率
     */
    private Double passRate;




}
