package com.example.oj.controller;

import com.example.oj.common.Result;
import com.example.oj.common.TestCaseResult;
import com.example.oj.domain.entity.CodeRecord;
import com.example.oj.domain.vo.CodeRecordVO;
import com.example.oj.service.ICodeRecordService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/record")
public class CodeRecordController {
    @Resource
    ICodeRecordService codeRecordService;
    /**
     * 查询当前用户所有提交记录
     */
    @GetMapping("/get/all")
    public Result<CodeRecordVO> getAllRecord(){
         return null;
    };
    /**
     * 查询当前用户对题目id的所有提交记录
     */
    @GetMapping("/get/list/{id}")
        public Result<CodeRecordVO> getRecordByUid(@PathVariable Long id){
        return null;
    };
    /**
    * 查询当前用户对题目id最新的提交记录
    */
    @GetMapping("/get/one/{id}")
    public Result codeRecordGetById(@PathVariable Long id) throws Exception {
        CodeRecord score = codeRecordService.codeRecordGetById(id);
        return Result.success(score);
    }

    /**
     * 查询提交的详细测试点信息
     * @param submissionId
     * @return
     */
    @GetMapping("/get/commits/{submissionId}")
    public Result<List<TestCaseResult>> getRecordBySubmissionId(@PathVariable Long submissionId) {
        List<TestCaseResult> testCaseResultListBySubmissionId = codeRecordService.getTestCaseResultListBySubmissionId(submissionId);
        return Result.success(testCaseResultListBySubmissionId);
    }

}
