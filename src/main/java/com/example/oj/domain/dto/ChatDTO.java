package com.example.oj.domain.dto;

import lombok.Data;

@Data
public class ChatDTO {


    /**
     * 用户id
     */
    private String id;
    /**
     * 内容
     */
    private String content;

    /**
     * 选择的智能体
     */
    private String Ai;
}
