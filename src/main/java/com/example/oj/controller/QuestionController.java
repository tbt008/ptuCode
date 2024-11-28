package com.example.oj.controller;


import com.example.oj.common.ErrorCode;
import com.example.oj.common.Result;
import com.example.oj.domain.dto.JudgeDTO;
import com.example.oj.domain.entity.CodeRecord;
import com.example.oj.domain.entity.Question;
import com.example.oj.exception.BusinessException;
import com.example.oj.service.IQuestionService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


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
    @Resource
    IQuestionService iQuestionService;

    /**
     * 提交代码
     * @param judgeDTO
     * @return
     */
    @PostMapping("/judge")
    public Result<Long> submitQuestion(@RequestBody JudgeDTO judgeDTO){
        // 参数校验
        if(judgeDTO.getCode()==null||judgeDTO.getQuestionId()<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //运行代码
        return Result.success(iQuestionService.submitQuestion(judgeDTO));
    }



    @GetMapping("/hello")
    public Result get(){
        Question one = iQuestionService.lambdaQuery().eq(Question::getTitleId, 1).one();
        return Result.success(one);
    }

    @GetMapping("codeRecordGetById/{submissionId}")
    public Result codeRecordGetById(@PathVariable Long submissionId) throws Exception {
        CodeRecord score = iQuestionService.codeRecordGetById(submissionId);
        return Result.success(score);
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
