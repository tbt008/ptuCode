package com.example.oj.controller;


import cn.hutool.Hutool;
import cn.hutool.http.server.HttpServerBase;
import com.example.oj.common.ErrorCode;
import com.example.oj.common.Result;
import com.example.oj.domain.dto.JudgeDTO;
import com.example.oj.domain.dto.QuestionDTO;
import com.example.oj.domain.entity.Question;
import com.example.oj.domain.entity.Tag;
import com.example.oj.domain.vo.QuestionVo;
import com.example.oj.exception.BusinessException;
import com.example.oj.service.IQuestionService;
import com.example.oj.service.IQuestionTagService;
import com.example.oj.service.ITagService;
import com.example.oj.service.impl.QuestionTagServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


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

        List<String> tag_names = questionDTO.getTag_names();
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

        List<String> tag_names = questionDTO.getTag_names();
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
