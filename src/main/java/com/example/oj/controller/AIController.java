package com.example.oj.controller;

import com.example.oj.common.Result;
import com.example.oj.domain.dto.ChatDTO;
import com.example.oj.service.AIService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/AI")
public class AIController {
    @Resource
    AIService aiService;
    @PostMapping("/chat")
    public Result XingHuoChat(@RequestBody ChatDTO chatDTO){
        if (chatDTO.getId() == null || chatDTO.getId().isEmpty()) {
            return Result.error("ID不能为空");
        }
        if (chatDTO.getContent().isEmpty()) return Result.error("内容不能为空");
        if (chatDTO.getAi() == null || chatDTO.getAi().isEmpty()) {
            chatDTO.setAi("智谱清言");
        }
        if(chatDTO.getAi().equals("讯飞星火")){
            return aiService.XinHuoChat(chatDTO.getId(),chatDTO.getContent());

        }
        else if(chatDTO.getAi().equals("智谱清言")){
            return aiService.ZhiPuChat(chatDTO.getId(),chatDTO.getContent());
        }
        else return Result.error("智能体选择有误");
    }
}
