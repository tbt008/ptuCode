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
        Double result = codeRecord.getResult();
        if (codeRecord == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        } else if (codeRecord.getStatus() == 1) {
            return codeRecord;
        } else if (result == -2) {
            return codeRecord;
        } else if (result >= 0) {
            return codeRecord;
        }
        String judgeInfo = codeRecord.getJudgeInfo();
        if (judgeInfo == null) {
           throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {

            JsonNode rootNode = objectMapper.readTree(judgeInfo);
            String err = rootNode.get("err").toString();
            System.out.println(err);

            if (err.equals("null")) {
                String data = rootNode.get("data").toString();
                List<TestCaseResult> testCaseResults = JSON.parseArray(data, TestCaseResult.class);
                if (testCaseResults.isEmpty()) {
                    return codeRecord;
                }
                Double total = 1.0 * testCaseResults.size();
                double score = 0.0;
                for (TestCaseResult testCaseResult : testCaseResults) {
                    if (testCaseResult.getResult() == 0) {
                        score = score + 100.0;
                    }
                }
                UpdateWrapper<CodeRecord> codeRecordUpdateWrapper = new UpdateWrapper<>();
                codeRecordUpdateWrapper.eq("id", submissionId);
                CodeRecord updateCodeRecord = new CodeRecord();
                updateCodeRecord.setResult(score / total);
                codeRecordMapper.update(updateCodeRecord, codeRecordUpdateWrapper);
                codeRecord.setResult(score / total);
                return codeRecord;
            }

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

}
