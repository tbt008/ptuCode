package com.example.oj.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.oj.common.*;
import com.example.oj.domain.dto.JudgeDTO;
import com.example.oj.domain.dto.QuestionDTO;
import com.example.oj.domain.dto.QuestionFilterDTO;
import com.example.oj.domain.entity.Question;
import com.example.oj.domain.vo.QuestionUserVO;
import com.example.oj.domain.vo.QuestionVo;
import com.example.oj.exception.BusinessException;
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
        return null;
//       return Result.success(iQuestionService.getQuestionVO(question));
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

        return Result.success();
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
        Boolean st = iQuestionService.lambdaQuery().eq(Question::getTitleName, questionDTO.getTitleName()).exists();
        if(st==true){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"题目名已经存在");
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionDTO, question);
        // 参数校验
        iQuestionService.validQuestion(question);
//  TODO 标签待定

//        TODO 获取创建人
        boolean vis = iQuestionService.save(question);
        if(vis==false){
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        Long questionId = Long.valueOf(question.getId());
        return Result.success(questionId);
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
        Question question = iQuestionService.lambdaQuery().eq(Question::getId, questionDTO.getId()).one();
        if(question==null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"题目不存在");
        }
        // 参数校验
        iQuestionService.validQuestion(question);

//TODO 标签

        Question nowQuestion = new Question();
        BeanUtils.copyProperties(questionDTO, nowQuestion);

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

    /**
     * 根据题目id上传测试数据
     * @param file
     * @param questionId
     * @return
     * @throws IOException
     */
     @PostMapping("/savefile")
     public Result<Integer> saveQuestionFile(@RequestParam("file") MultipartFile file, @RequestParam("path") Long questionId) throws IOException {

         Question question = iQuestionService.lambdaQuery().eq(Question::getId, questionId).one();

         if (!permissionUtils.problemController(question.getCreateUser())) {
             throw new BusinessException(ErrorCode.PERMISSION_ERROR);
         }

         return Result.success(iQuestionService.saveFile(questionId, file));
     }

    /**
     * 获取题目详细列表
     * @param questionId
     * @return
     */
     @PostMapping("/questionlist")
     public Result questionList(@RequestBody Map<String, List<Long>> questionId) {
         List<Long> list = questionId.get("questionId");
         List<Question> questionList = iQuestionService.getQuestionList(list);
         for (Question question : questionList) {
             if (question.getStatus() == 1) {
                 question.setAnswer("");
             }
         }
         return Result.success(iQuestionService.getQuestionList(list));
     }

}
