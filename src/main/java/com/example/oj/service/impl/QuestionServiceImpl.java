package com.example.oj.service.impl;


import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.oj.common.ErrorCode;
import com.example.oj.common.Language;
import com.example.oj.domain.dto.JudgeDTO;
import com.example.oj.domain.entity.CodeRecord;
import com.example.oj.domain.entity.Question;
import com.example.oj.exception.BusinessException;
import com.example.oj.mapper.CodeRecordMapper;
import com.example.oj.mapper.QuestionMapper;
import com.example.oj.service.IQuestionService;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tbt
 * @since 2024-11-24
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements IQuestionService {

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @Resource
    private QuestionMapper questionMapper;

    @Resource
    private CodeRecordMapper codeRecordMapper;


    @Override
    public Long submitQuestion(JudgeDTO judgeDTO) {

//        校验语言是否正常
        Integer language = judgeDTO.getLanguage();
        if(!Language.judgeById(language)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

//        查找题目是否存在
        Question question = questionMapper.selectById(judgeDTO.getQuestionId());
        if (Objects.isNull(question)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
//         存在的话，获取一下题目的限制条件
        Integer memoryLimit = question.getMemoryLimit();
        Integer timeLimit = question.getTimeLimit();

//    新增一条提交记录
        CodeRecord codeRecord = new CodeRecord();
        codeRecord.setResult(-1.0);
        codeRecord.setCode(judgeDTO.getCode());
        codeRecord.setLanguage(language);
        codeRecord.setQuestionId(judgeDTO.getQuestionId());
//     TODO  登录后获取用户id
        codeRecord.setUserId(1L);

        LocalDateTime now = LocalDateTime.now();
        codeRecord.setCreateTime(now);
        codeRecord.setUpdateTime(now);
//        状态待判题
        codeRecord.setStatus(0);

//        判题机跑代码
        codeRecordMapper.insert(codeRecord);
        Long returnId = codeRecord.getId();

                executorService.submit(new Runnable() {
            @Override
            public void run() {
                String langConfig = getLanguageConfig(language);
                try {
                    // 封装post基础数据
                    HashMap<String, Object> questionMap = getHashMap(1000 * timeLimit, 1048576 * memoryLimit, judgeDTO.getCode(), judgeDTO.getQuestionId().toString(), false);

                    // 封装post language_config
                    JSONObject jsonObject = JSONObject.parseObject(langConfig);
                    JSONObject result = new JSONObject(questionMap);
                    // 基础数据和language_config合并
                    result.putAll(jsonObject);

                    // 创建 HttpClient 和 HttpPost
                    HttpClient httpClient = HttpClientBuilder.create().build();
                    HttpPost httpPost = getHttpPost();

                    // 设置请求体
                    StringEntity entity = new StringEntity(result.toString());
                    httpPost.setEntity(entity);

                    // 设置请求的超时配置
                    RequestConfig requestConfig = RequestConfig.custom()
                            .setSocketTimeout(6000)
                            .setConnectTimeout(6000)
                            .build();
                    httpPost.setConfig(requestConfig);

                    // 发送请求
                    HttpResponse response = httpClient.execute(httpPost);
                    String strResult = EntityUtils.toString(response.getEntity());
                    UpdateWrapper<CodeRecord>  codeRecordUpdateWrapper = new UpdateWrapper<>();
                    codeRecordUpdateWrapper.eq("id", returnId);
                    CodeRecord updateCodeRecord = new CodeRecord();
                    updateCodeRecord.setStatus(2);
                    updateCodeRecord.setResult(-1);
                    updateCodeRecord.setJudgeInfo(strResult);
                    codeRecordMapper.update(updateCodeRecord, codeRecordUpdateWrapper);
                } catch (Exception e) {
                    UpdateWrapper<CodeRecord> codeRecordUpdateWrapper = new UpdateWrapper<>();
                    codeRecordUpdateWrapper.eq("id", returnId);
                    CodeRecord updateCodeRecord = new CodeRecord();
                    updateCodeRecord.setResult(-1);
                    updateCodeRecord.setStatus(3);
                    codeRecordMapper.update(updateCodeRecord, codeRecordUpdateWrapper);
                    e.printStackTrace();
                }
            }
        });

        return returnId;
    }

    private String getLanguageConfig (Integer language) {
        String langConfig = "";
        if (Objects.equals(language, Language.C.getId())) {
            langConfig = Language.C.getLangConfig();
        } else if (Objects.equals(language, Language.CPP.getId())) {
            langConfig = Language.CPP.getLangConfig();
        } else if (Objects.equals(language, Language.JAVA.getId())) {
            langConfig = Language.JAVA.getLangConfig();
        } else if (Objects.equals(language, Language.PYTHON.getId())) {
            langConfig = Language.PYTHON.getLangConfig();
        }
        return langConfig;
    }

    private HashMap<String, Object> getHashMap(Integer timeLimit, Integer memoryLimit, String code, String questionId, boolean output) {
        HashMap<String, Object> questionMap = new HashMap<>();
        questionMap.put("max_cpu_time", timeLimit);
        questionMap.put("max_memory", memoryLimit);
        questionMap.put("src", code);
        questionMap.put("test_case_id", questionId);
        if (output) {
            questionMap.put("output", true);
        }
        return questionMap;
    }

    private HttpPost getHttpPost() {
        String url = "http://120.26.170.155:12358/judge";
        HttpPost httpPost = new HttpPost(url);
        // 设置请求头
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("X-Judge-Server-Token", "415d45f78c7799b50958fba9934971842612a63911805f5345118264dda7bebc");
        return httpPost;
    }
}
