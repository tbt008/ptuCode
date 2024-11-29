package com.example.oj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.oj.common.Result;
import com.example.oj.domain.entity.ChatHistory;

public interface AIService extends IService<ChatHistory> {
    /**
     * 获取讯飞星火的回答
     * @param content
     * @return
     */
    Result XinHuoChat(String content);
    /**
     * 获取智谱清言的回答
     * @param content
     * @return
     */
    Result ZhiPuChat(String Id,String content);
}
