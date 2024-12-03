package com.example.oj.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.oj.annotation.AuthCheck;
import com.example.oj.common.*;
import com.example.oj.domain.dto.JudgeDTO;
import com.example.oj.domain.dto.QuestionDTO;
import com.example.oj.domain.dto.QuestionFilterDTO;
import com.example.oj.domain.entity.Question;
import com.example.oj.domain.vo.QuestionUserVO;
import com.example.oj.domain.vo.QuestionVo;
import com.example.oj.exception.BusinessException;
import com.example.oj.service.IContestService;
import com.example.oj.service.IQuestionService;
import com.example.oj.service.IQuestionTagService;
import com.example.oj.utils.PermissionUtils;
import org.springframework.beans.BeanUtils;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;

import java.io.UnsupportedEncodingException;
import java.util.Map;


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
    private IQuestionService iQuestionService;

    @Resource
    private IQuestionTagService iQuestionTagService;

    private PermissionUtils permissionUtils;

    @Resource
    private IContestService contestService;

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


    /**
     * 获得测试样例
     * @param inputCase
     * @return
     */
    @PostMapping("/test")
    public Result<TestCaseResult> submitQuestionTest(@RequestBody InputCase inputCase) throws UnsupportedEncodingException {
        String input = inputCase.getInput();
        Integer language = inputCase.getLanguage();;
        String code = inputCase.getCode();
        if (input==null) {
            input = "";
        }
        TestCaseResult answer = iQuestionService.getOutputByInput(input, language, code);
        return Result.success(answer);
    }


    /**
     * 搜索题目
     * @param id
     * @retrun questionVo
     */
    @GetMapping("/{id}")
    public Result<QuestionVo> getById(@PathVariable("id") Long id, HttpServletRequest request){
        Question question = iQuestionService.lambdaQuery().eq(Question::getId, id).one();
        if(question==null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        if(question.getStatus()==1){
            return Result.error(null);
        }
        QuestionVo questionVo = new QuestionVo();

        BeanUtil.copyProperties(question, questionVo);

        return Result.success(questionVo);
    }
    /**
     * 题目列表(用户显示
     * @param questionFilterDTO
     * @retrun Page<QuestionVo>
     */
    @PostMapping("/list")
    public Result<List<QuestionUserVO>> QuestionList(@RequestBody QuestionFilterDTO questionFilterDTO) {


        if (questionFilterDTO == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long start = questionFilterDTO.getPageStart();
        long size = questionFilterDTO.getPageSize();

        Page<Question> questionPage = iQuestionService.page(
                new Page<>(start, size),
                iQuestionService.getListWrapper(questionFilterDTO));
        List<QuestionUserVO> records = iQuestionService.getQuestionPageVO(questionPage).getRecords();
//        过滤掉状态
//        已通过未通过
        return Result.success(records);
    }

    /**
     * 获取题目详细列表
     * @param questionId
     * @return
     */
     @PostMapping("/contest/list")
     public Result questionList(@RequestBody Map<String, List<Long>> questionId) {
         List<Long> list = questionId.get("questionId");
         List<Question> questionList = iQuestionService.getQuestionList(list);
         return Result.success(questionList);
     }

}
