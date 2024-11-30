package com.example.oj.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.oj.common.ErrorCode;
import com.example.oj.common.InputCase;
import com.example.oj.common.Result;
import com.example.oj.common.TestCaseResult;
import com.example.oj.domain.dto.JudgeDTO;
import com.example.oj.domain.dto.QuestionDTO;
import com.example.oj.domain.entity.Question;
import com.example.oj.domain.vo.QuestionVo;
import com.example.oj.exception.BusinessException;
import com.example.oj.service.IQuestionService;
import com.example.oj.service.IQuestionTagService;
import org.springframework.beans.BeanUtils;

import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import java.io.UnsupportedEncodingException;


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

    /**
     * 提交代码
     * @param judgeDTO
     * @return
     */
    @PostMapping("/judge")
    public Result<Long> submitQuestion(@RequestBody JudgeDTO judgeDTO){
           System.out.println(judgeDTO);
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
    @GetMapping("/{question_id}")
    public Result<QuestionVo> getById(@PathVariable("question_id") Long id, HttpServletRequest request){
        Question question = iQuestionService.lambdaQuery().eq(Question::getTitleId, id).one();
        if(question==null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return Result.success(iQuestionService.getQuestionVO(question));
    }
    /**
     * 题目列表
     * @param questionDTO
     * @retrun Page<QuestionVo>
     */
    @PostMapping("/list")
    public Result<Page<QuestionVo>> QuestionList(@RequestBody QuestionDTO questionDTO, HttpServletRequest request) {
        if (questionDTO == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long start = questionDTO.getPageStart();
        long size = questionDTO.getPageSize();

        Page<Question> questionPage = iQuestionService.page(
                new Page<>(start, size),
                iQuestionService.getListWrapper(questionDTO));
        return Result.success(iQuestionService.getQuestionPageVO(questionPage));
    }

    /**
     * 新增题目（管理员）
     * @param questionDTO
     * @retrun questionid
     */
    @PostMapping("/add")
    public Result<Long> addQuestion(@RequestBody QuestionDTO questionDTO, HttpServletRequest request) {
        if (questionDTO == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 判断是否存在
        Boolean st = iQuestionService.lambdaQuery().eq(Question::getTitleId, questionDTO.getTitleId()).exists();
        if(st==true){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"题目id已经存在");
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionDTO, question);
        // 参数校验
        iQuestionService.validQuestion(question);

        List<String> tag_names = questionDTO.getTagNames();
        if(tag_names!=null){
            iQuestionTagService.savetag(questionDTO.getTitleId(),tag_names);
        }

        //TODO judgeCase
        boolean vis = iQuestionService.save(question);
        if(vis==false){
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        long questionid = question.getId();
        return Result.success(questionid);
    }

    /**
     * 修改题目 （管理员）
     * @param questionDTO
     * @return
     */
    @PutMapping("/update")
    public Result<Boolean> updateQuestion(@RequestBody QuestionDTO questionDTO,HttpServletRequest request) {
        if (questionDTO == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 判断是否存在
        Question question = iQuestionService.lambdaQuery().eq(Question::getTitleId, questionDTO.getTitleId()).one();
        if(question==null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"题目不存在");
        }
        // 参数校验
        iQuestionService.validQuestion(question);

        List<String> tag_names = questionDTO.getTagNames();
        if(tag_names!=null){
            iQuestionTagService.removeBytitleId(questionDTO.getTitleId());
            iQuestionTagService.savetag(questionDTO.getTitleId(),tag_names);
        }

        //TODO judgeCase
        Question nowQuestion = new Question();
        BeanUtils.copyProperties(questionDTO, nowQuestion);
        nowQuestion.setId(question.getId());
        return Result.success(iQuestionService.updateById(nowQuestion));
    }
     /**
     * 删除题目 （管理员）
      * @param id
      * @return
     */
     @DeleteMapping("/delete/{title_id}")
     public Result<Boolean> deleteQuestion(@PathVariable("title_id") Long id, HttpServletRequest request) {
         Question question = iQuestionService.lambdaQuery().eq(Question::getTitleId, id).one();
         if(question==null){
             throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
         }
         return Result.success(iQuestionService.removeQuestion(question));
     }

}
