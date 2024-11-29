package com.example.oj.service.impl;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.oj.common.ErrorCode;
import com.example.oj.common.TestCaseResult;
import com.example.oj.domain.entity.CodeRecord;
import com.example.oj.exception.BusinessException;
import com.example.oj.mapper.CodeRecordMapper;
import com.example.oj.service.ICodeRecordService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
public class CodeRecordServiceImpl extends ServiceImpl<CodeRecordMapper, CodeRecord> implements ICodeRecordService {

    @Resource
    CodeRecordMapper codeRecordMapper;

    @Override
    public CodeRecord codeRecordGetById(Long submissionId) {
        if(submissionId==null||submissionId<=0){
              throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        CodeRecord codeRecord = codeRecordMapper.selectById(submissionId);
        if (codeRecord == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 查找分数是否已经计算过
        Double result = codeRecord.getResult();
        if (codeRecord.getStatus() == 1) {
            // status为1代表还在判题
            return codeRecord;
        } else if (result == -2) {
            // status 为-2代表代码ce
            return codeRecord;
        } else if (result >= 0) {
            // result >= 0代表已经有分数可以直接返回
            return codeRecord;
        }
        String judgeInfo = codeRecord.getJudgeInfo();
        if (judgeInfo == null) {
           throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // 把judgeInfo转成json
            JsonNode rootNode = objectMapper.readTree(judgeInfo);
            // 获取err
            String err = rootNode.get("err").toString();
            System.out.println(err);

            // 为null代表代码运行正常
            if (err.equals("null")) {
                // 从json格式中获取string类型data
                String data = rootNode.get("data").toString();

                // 把data转换成List<TestCaseResult>
                List<TestCaseResult> testCaseResults = JSON.parseArray(data, TestCaseResult.class);

                Double total = 1.0 * testCaseResults.size();
                double score = 0.0;
                // 从testCaseResults获得result分数
                for (TestCaseResult testCaseResult : testCaseResults) {
                    if (testCaseResult.getResult() == 0) {
                        score = score + 100.0;
                    }
                }
                double finalScore;
                //为空直接设置100分
                if (testCaseResults.isEmpty()) {
                    finalScore = 100.0;
                } else {
                    finalScore = score / total;
                }
                //更新数据库分数
                UpdateWrapper<CodeRecord> codeRecordUpdateWrapper = new UpdateWrapper<>();
                codeRecordUpdateWrapper.eq("id", submissionId);
                CodeRecord updateCodeRecord = new CodeRecord();
                updateCodeRecord.setResult(finalScore);
                codeRecordMapper.update(updateCodeRecord, codeRecordUpdateWrapper);

                codeRecord.setResult(finalScore);
                return codeRecord;
            }

            // 不为空代表代码CE
            UpdateWrapper<CodeRecord> codeRecordUpdateWrapper = new UpdateWrapper<>();
            codeRecordUpdateWrapper.eq("id", submissionId);
            CodeRecord updateCodeRecord = new CodeRecord();
            updateCodeRecord.setResult(-2.0);
            codeRecordMapper.update(updateCodeRecord, codeRecordUpdateWrapper);
            codeRecord.setResult(-2.0);
            return codeRecord;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<TestCaseResult> getTestCaseResultListBySubmissionId(Long submissionId) {

        CodeRecord codeRecord = codeRecordMapper.selectById(submissionId);
        String judgeInfo = codeRecord.getJudgeInfo();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = null;
        try {
            rootNode = objectMapper.readTree(judgeInfo);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        // 获取err
        String err = rootNode.get("err").toString();
        if (err.equals("null")) {
            List<TestCaseResult> testCaseResults = JSON.parseArray(rootNode.get("data").toString(), TestCaseResult.class);
            return testCaseResults;
        }
        return null;
    }

}
