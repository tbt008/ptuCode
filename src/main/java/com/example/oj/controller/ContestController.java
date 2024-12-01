package com.example.oj.controller;

import com.example.oj.common.Page;
import com.example.oj.common.Result;
import com.example.oj.service.IContestService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/contest")
public class ContestController {

    @Resource
    private IContestService contestService;

    @GetMapping
    private Result getContests(@RequestBody Page page) {
        return Result.success(contestService.getContestOverviewVOByPageAndPageNum(page.getPageNum(), page.getPageSize()));
    }

    @GetMapping("/racepage/{contestId}")
    private Result racepage(@PathVariable Integer contestId) {
        return Result.success(contestService.getContestById(contestId));
    }

    @PostMapping("isinvite")
    private Result isvited(@RequestBody Integer contestId) {
        return Result.success(contestService.isInvite(contestId));
    }
}
