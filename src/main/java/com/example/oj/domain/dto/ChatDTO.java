package com.example.oj.domain.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("AI")
public class ChatDTO {

    /**
     * 内容
     */
    private String content;

    /**
     * 选择的智能体
     */
    private String Ai;
}
