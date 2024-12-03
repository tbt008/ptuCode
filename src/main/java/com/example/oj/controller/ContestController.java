package com.example.oj.controller;

import com.example.oj.common.ErrorCode;
import com.example.oj.common.Page;
import com.example.oj.common.Result;
import com.example.oj.domain.dto.ContestJudgeDTO;
import com.example.oj.domain.entity.Contest;
import com.example.oj.domain.entity.Question;
import com.example.oj.domain.vo.ContestVO;
import com.example.oj.exception.BusinessException;
import com.example.oj.service.IContestService;
import com.example.oj.service.IQuestionService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/contest")
public class ContestController {

    @Resource
    private IContestService contestService;

    @Resource
    private IQuestionService questionService;

    /**
     * 获取比赛基础信息
     * @param contestId
     * @return
     */
    @GetMapping("/racepage/{contestId}")
    private Result racepage(@PathVariable Long contestId) {
        if (contestId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Contest contestById = contestService.getContestById(contestId);
        return Result.success(contestById);
    }

    /**
     * 返回比赛题目信息
     * @param contestId
     * @return
     */
    @PostMapping("/racepage/{contestId}/quesion")
    private Result quesionList(@PathVariable Long contestId) {

        if (contestService.isInvite(contestId) != 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        List<Question> questionList = questionService.getQuestionListById(contestId);
        return Result.success(questionList);
    }




    /**
     * 0代表被可以进, 1代表未输入密码, 2代表未被邀请, 3代表比赛未开始, 4代表比赛结束
     * @param contestId
     * @return
     */
    @PostMapping("isinvite")
    private Result isvited(@RequestBody Long contestId) {
        return Result.success(contestService.isInvite(contestId));
    }

    /**
     * 赛时提交
     * @param contestJudgeDTO
     * @return
     */
    @PostMapping("/question/judge")
    private Result judge(@RequestBody ContestJudgeDTO contestJudgeDTO) {
        return null;
    }


}
