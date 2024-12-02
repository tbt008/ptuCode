package com.example.oj.controller;

import com.example.oj.common.Page;
import com.example.oj.common.Result;
import com.example.oj.service.IContestService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/contest")
public class ContestController {

    @Resource
    private IContestService contestService;

    /**
     * 查找比赛大致信息
     * @param page
     * @return
     */
    @GetMapping
    private Result getContests(@RequestBody Page page) {
        return Result.success(contestService.getContestOverviewVOByPageAndPageNum(page.getPageNum(), page.getPageSize()));
    }

    /**
     * 获取比赛详细信息
     * @param contestId
     * @return
     */
    @GetMapping("/racepage/{contestId}")
    private Result racepage(@PathVariable Long contestId) {
        if (contestId == null) {
            return Result.error("contestId is null");
        }
        return Result.success(contestService.getContestById(contestId));
    }

    /**
     * 是否被邀请
     * @param contestId
     * @return
     */
    @PostMapping("isinvite")
    private Result isvited(@RequestBody Long contestId) {
        return Result.success(contestService.isInvite(contestId));
    }
}
