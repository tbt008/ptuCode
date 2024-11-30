package com.example.oj.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@TableName("chat_history")
public class ChatHistory {
    /**
     * 用户唯一标识符，用于区分不同用户
     */
    private String Id;

    /**
     * 存储用户的聊天内容
     */
    @TableField("chat_text")
    private String chatText;

    /**
     * 选择的AI
     */
    //
    @TableField("ai")
    private String ai;

    /**
     * 记录消息的发送时间，默认当前时间
     */
    @TableField("timestamp")
    private LocalDateTime timestamp;
}
