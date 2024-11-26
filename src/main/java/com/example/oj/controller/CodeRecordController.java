package com.example.oj.controller;

import com.example.oj.common.Result;
import com.example.oj.domain.vo.CodeRecordVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/record")
public class CodeRecordController {
/**
 * 查询当前用户所有提交记录
 */
@GetMapping("/get/all")
    public Result<CodeRecordVO> getAllRecord(){
     return null;
};
/**
 * 查询当前用户对题目id的提交记录
 */
@GetMapping("/get/question/{id}")
    public Result<CodeRecordVO> getRecordByUid(@PathVariable Long id){
    return null;
};

}
