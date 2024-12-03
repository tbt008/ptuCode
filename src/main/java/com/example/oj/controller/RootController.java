package com.example.oj.controller;

import com.example.oj.annotation.AuthCheck;
import com.example.oj.common.ErrorCode;
import com.example.oj.common.PageLimit;
import com.example.oj.common.Permission;
import com.example.oj.common.Result;
import com.example.oj.domain.dto.CreateContestDTO;
import com.example.oj.domain.dto.QuestionDTO;
import com.example.oj.domain.dto.UserRegisterDTO;
import com.example.oj.domain.entity.Contest;
import com.example.oj.domain.entity.ContestQuestion;
import com.example.oj.domain.entity.Question;
import com.example.oj.domain.entity.QuestionInfo;
import com.example.oj.domain.vo.ContestVO;
import com.example.oj.domain.vo.UserRegisterVO;
import com.example.oj.exception.BusinessException;
import com.example.oj.service.IContestService;
import com.example.oj.service.IQuestionService;
import com.example.oj.service.IUserService;
import com.example.oj.utils.ConvertBeanUtils;
import com.example.oj.utils.PermissionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

//管理员权限
@RestController
@RequestMapping("/root")
public class RootController {


    @Resource
    private IUserService userService;

    @Resource
    private IQuestionService iQuestionService;

    @Resource
    private IContestService iContestService;


    PermissionUtils permissionUtils;
    /**
     * 根据id查找用户所有信息
     */
    @GetMapping("/get/{id}")
    public Result getUserById(@PathVariable("id") Long id){
        return null;
    }
    /**
     * 根据id设置用户停用
     */
    @GetMapping("/stop/{id}")
    public Result stopUserById(@PathVariable("id") Long id){
        return null;
    }

    /**
     * 删除用户
     */
    @PostMapping("/delete/{id}")
    public Result deleteUserById(@PathVariable("id") Long id ) {
      return null;
    }

    /**
     * 更新用户
     */
    @PostMapping("/update")
    public Result updateUser(@RequestBody UserRegisterDTO userRegisterDTO){
      return null;
    }


    /**
     * 分页获取用户列表
     */
    @PostMapping("/list/page")
    public Result listUserByPage(@RequestBody PageLimit pageLimit){
     return null;
    }


    /**
     * 管理员excel批量导入人数
     * @return
     */
    @PostMapping("/registerbatch")
    @AuthCheck(permission = Permission.USER_CONTROLLER)
    public Result registerBatch(@RequestParam("file") MultipartFile file) throws IOException {

        if (!permissionUtils.UserController()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<UserRegisterVO> registerVOList = userService.registerBatch(file);
        if (registerVOList == null || registerVOList.isEmpty()) {
            return Result.success();
        }
        return Result.error(404, registerVOList.toString());
    }


    /**
     * 新增题目（管理员）
     * @param questionDTO
     * @retrun questionid
     */
    @PostMapping("/question/add")
    @AuthCheck(permission = Permission.QUESTION_EDITOR)
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
    @PutMapping("/question/update")
    @AuthCheck(permission = Permission.QUESTION_EDITOR)
    public Result<Boolean> updateQuestion(@RequestBody QuestionDTO questionDTO,HttpServletRequest request) {
        if (questionDTO == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 判断是否存在
        Question question = iQuestionService.lambdaQuery().eq(Question::getId, questionDTO.getId()).eq(Question::getIsDeleted,0).one();
        if(question==null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"题目不存在");
        }
//        权限校验
        PermissionUtils.checkUserId(question.getCreateUser());
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
    @DeleteMapping("/question/delete/{title_id}")
    @AuthCheck(permission = Permission.QUESTION_EDITOR)
    public Result<Boolean> deleteQuestion(@PathVariable("title_id") Long id, HttpServletRequest request) {
        Question question = iQuestionService.lambdaQuery().eq(Question::getTitleId, id).eq(Question::getIsDeleted,0).one();
        if(question==null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        //        权限校验
        PermissionUtils.checkUserId(question.getCreateUser());

        return Result.success(iQuestionService.removeQuestion(question));
    }

    /**
     * 根据题目id上传测试数据
     * @param file
     * @param questionId
     * @return
     * @throws IOException
     */
    @PostMapping("/quesion/save/file")
    public Result<Integer> saveQuestionFile(@RequestParam("file") MultipartFile file, @RequestParam("path") Long questionId) throws IOException {

        Question question = iQuestionService.lambdaQuery().eq(Question::getId, questionId).one();

        if (!permissionUtils.problemController(question.getCreateUser())) {
            throw new BusinessException(ErrorCode.PERMISSION_ERROR);
        }

        return Result.success(iQuestionService.saveFile(questionId, file));
    }

    /**
     * 创建比赛
     * @param createContestDTO
     * @return
     */
    @PostMapping("/contest/create")
    @AuthCheck(permission = Permission.CONTEST_CONTROLLER)
    public Result createContest(@RequestBody CreateContestDTO createContestDTO) {
        if (createContestDTO == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        iContestService.createContest(createContestDTO);
        return Result.success();
    }

    /**
     * 根据比赛id查看具体信息
     * @param contestId
     * @return
     */
    @PostMapping("/contest/get")
    @AuthCheck(permission = Permission.CONTEST_CONTROLLER)
    public Result getContestById(@RequestParam("id") Long contestId) {
        if (contestId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Contest contest = iContestService.getContestById(contestId);
        ContestVO contestVO = ConvertBeanUtils.convert(contest, ContestVO.class);
        List<QuestionInfo> questionIdList = iQuestionService.getQuestionIdListByContestId(contestId);
        contestVO.setQuestionInfoList(questionIdList);
        if (!permissionUtils.contestController(contest.getCreateUser())) {
            throw new BusinessException(ErrorCode.PERMISSION_ERROR);
        }
        return Result.success(contestVO);
    }

}
