package com.example.oj.service.impl;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.oj.common.ErrorCode;
import com.example.oj.common.Language;
import com.example.oj.common.TestCaseResult;
import com.example.oj.domain.dto.JudgeDTO;
import com.example.oj.domain.entity.CodeRecord;
import com.example.oj.domain.entity.Question;
import com.example.oj.exception.BusinessException;
import com.example.oj.mapper.CodeRecordMapper;
import com.example.oj.mapper.QuestionMapper;
import com.example.oj.service.IQuestionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.*;
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
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        String code = "#include <iostream>\nusing namespace std;\n\nint main() {\n    int a, b;\n    cin >> a >> b;\n    cout << a + b << endl;\n    return 0;\n}";
//        String input = "1 2";
//        Integer language = Language.CPP.getId();
//        TestCaseResult outputByInput = getOutputByInput(input, language, code);
//        System.out.println(outputByInput);
//    }

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

    @Override
    public TestCaseResult getOutputByInput(String input, Integer language, String code) throws UnsupportedEncodingException {
        String langConfig = getLanguageConfig(language);
        String testCaseId = UUID.randomUUID().toString();
        HashMap<String, Object> body = getHashMap(1000, 1048576 * 256, code, testCaseId, true);
        // 封装post language_config
        JSONObject jsonObject = JSONObject.parseObject(langConfig);
        JSONObject result = new JSONObject(body);
        // 基础数据和language_config合并
        result.putAll(jsonObject);

        try {
            uploadFile(testCaseId, "1.in", input);
            uploadFile(testCaseId, "1.out", "");
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
            HttpResponse response = httpClient.execute(httpPost);
            String strResult = EntityUtils.toString(response.getEntity());

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = null;
            try {
                rootNode = objectMapper.readTree(strResult);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            // 获取err
            String err = rootNode.get("err").toString();
            if (err.equals("null")) {
                List<TestCaseResult> testCaseResults = JSON.parseArray(rootNode.get("data").toString(), TestCaseResult.class);
                if (testCaseResults.size() > 0) {
                    return testCaseResults.get(0);
                }
            }

        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            deleteFolder(testCaseId);
            File in = new File("1.in");
            File out = new File("1.out");
            in.delete();
            out.delete();
        }
        TestCaseResult testCaseResult = new TestCaseResult();
        testCaseResult.setError(-12);
        return testCaseResult;
    }

    private void deleteFolder(String folderName) throws UnsupportedEncodingException {
        String url = "http://120.26.170.155:12138/delete_folder";
        HttpPost httpPost = new HttpPost(url);
        // 设置请求头
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/json");

        HashMap<String, String> postEntity = new HashMap<>();
        postEntity.put("folder_name", folderName);

        JSONObject result = new JSONObject(postEntity);
        StringEntity entity = new StringEntity(result.toString());


        httpPost.setEntity(entity);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpResponse response = httpClient.execute(httpPost);  // 执行请求
            // 打印响应状态码
            String strResult = EntityUtils.toString(response.getEntity());
            int statusCode = response.getStatusLine().getStatusCode();
            System.out.println("Response Code: " + statusCode);
            System.out.println(strResult);
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    private void uploadFile(String path, String filename, String input) throws UnsupportedEncodingException {

        File file = new File(filename);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(input);  // 将 input 字符串写入文件
        } catch (IOException e) {
            e.printStackTrace();
        }

        String url = "http://120.26.170.155:12138/upload";
        HttpPost httpPost = new HttpPost(url);
        // 设置请求头
        httpPost.setHeader("Accept", "application/json");

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        if (file.exists() && file.length() > 0) {
            System.out.println("File exists and has content.");
        } else {
            System.out.println("File does not exist or is empty.");
        }
        builder.addPart("file", new FileBody(file));  // 添加文件
        builder.addPart("path", new StringBody(path));  // 添加路径字段（传递路径参数）


        // 将构建的请求体设置到 HttpPost 中
        HttpEntity entity = builder.build();
        httpPost.setEntity(entity);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpResponse response = httpClient.execute(httpPost);  // 执行请求

            // 打印响应状态码
            String strResult = EntityUtils.toString(response.getEntity());
            int statusCode = response.getStatusLine().getStatusCode();
            System.out.println("Response Code: " + statusCode);
            System.out.println(strResult);
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
