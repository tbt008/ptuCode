package com.example.oj.controller;

import com.example.oj.common.Result;
import com.example.oj.domain.dto.ChatDTO;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/AI")
public class AIController {
//    @Resource
//    AIService aiService;
    @PostMapping("/chat")
    public Result XingHuoChat(@RequestBody ChatDTO chatDTO){
        if(chatDTO.getAi().equals("讯飞星火")){
//            return aiService.XinHuoChat(chatDTO.getContent());
            return null;
        }
        return Result.error("智能体选择有误");
    }
}
