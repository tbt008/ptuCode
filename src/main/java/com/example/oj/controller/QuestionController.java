package com.example.oj.controller;


import com.example.oj.common.ErrorCode;
import com.example.oj.common.Result;
import com.example.oj.domain.dto.JudgeDTO;
import com.example.oj.domain.entity.Question;
import com.example.oj.exception.BusinessException;
import com.example.oj.service.IQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author tbt
 * @since 2024-11-24
 */
@RestController
@RequestMapping("/question")
public class QuestionController {
    @Autowired
    IQuestionService iQuestionService;

    /**
     * 提交代码
     * @param judgeDTO
     * @return
     */
       @PostMapping("/judge")
    public Result submitQuestion(@RequestBody JudgeDTO judgeDTO){
//           参数校验
if(judgeDTO.getCode()==null||judgeDTO.getQuestionId()<=0){
    throw new BusinessException(ErrorCode.PARAMS_ERROR);
}
//运行代码
        return   Result.success(iQuestionService.submitQuestion(judgeDTO));

       }
    /**
     * 搜索题目
     */
    /**
     * 新增题目（管理员）
     */
 /**
     * 修改题目 （管理员）
     */
     /**
     * 删除题目 （管理员）
     */

}
