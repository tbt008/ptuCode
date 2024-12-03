package com.example.oj.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.oj.common.BaseContext;
import com.example.oj.common.ErrorCode;
import com.example.oj.common.Language;
import com.example.oj.common.TestCaseResult;
import com.example.oj.domain.dto.JudgeDTO;
import com.example.oj.domain.dto.QuestionFilterDTO;
import com.example.oj.domain.entity.CodeRecord;
import com.example.oj.domain.entity.ContestQuestion;
import com.example.oj.domain.entity.Question;
import com.example.oj.domain.entity.QuestionInfo;
import com.example.oj.domain.vo.QuestionUserVO;
import com.example.oj.exception.BusinessException;
import com.example.oj.mapper.CodeRecordMapper;
import com.example.oj.mapper.ContestQuestionMapper;
import com.example.oj.mapper.QuestionMapper;
import com.example.oj.service.IQuestionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import com.example.oj.service.IQuestionTagService;
import com.example.oj.utils.SqlUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import org.springframework.stereotype.Service;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
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

    @Resource
    private IQuestionTagService iQuestionTagService;

    @Resource
    private ContestQuestionMapper contestQuestionMapper;

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

        // 新增一条提交记录
        CodeRecord codeRecord = new CodeRecord();
        codeRecord.setResult(-1.0);
        codeRecord.setCode(judgeDTO.getCode());
        codeRecord.setLanguage(language);
        codeRecord.setQuestionId(judgeDTO.getQuestionId());
        codeRecord.setUserId(BaseContext.getUserInfo().getUserId());

        LocalDateTime now = LocalDateTime.now();
        codeRecord.setCreateTime(now);
        codeRecord.setUpdateTime(now);
        // 状态待判题
        codeRecord.setStatus(0);

        // 判题机跑代码
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
                            .setSocketTimeout(60000)
                            .setConnectTimeout(60000)
                            .build();
                    httpPost.setConfig(requestConfig);

                    // 发送请求
                    HttpResponse response = httpClient.execute(httpPost);
                    String strResult = EntityUtils.toString(response.getEntity());
                    UpdateWrapper<CodeRecord> codeRecordUpdateWrapper = new UpdateWrapper<>();
                    codeRecordUpdateWrapper.eq("id", returnId);
                    CodeRecord updateCodeRecord = new CodeRecord();
                    updateCodeRecord.setStatus(2);
                    updateCodeRecord.setResult(-1);
                    updateCodeRecord.setJudgeInfo(strResult);
                    updateCodeRecord.setUpdateTime(LocalDateTime.now());
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
                    .setSocketTimeout(10000)
                    .setConnectTimeout(10000)
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
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    private void uploadFile(String path, String filename, String input) throws UnsupportedEncodingException {

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));

        String url = "http://120.26.170.155:12138/upload";
        HttpPost httpPost = new HttpPost(url);
        // 设置请求头
        httpPost.setHeader("Accept", "application/json");

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        // 使用 InputStreamBody 直接传输流数据
        builder.addPart("file", new InputStreamBody(byteArrayInputStream, ContentType.APPLICATION_OCTET_STREAM, filename));  // 通过流传输文件
        builder.addPart("path", new StringBody(path));  // 添加路径字段（传递路径参数）


        // 将构建的请求体设置到 HttpPost 中
        HttpEntity entity = builder.build();
        httpPost.setEntity(entity);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpResponse response = httpClient.execute(httpPost);  // 执行请求

            // 打印响应状态码
            String strResult = EntityUtils.toString(response.getEntity());
            int statusCode = response.getStatusLine().getStatusCode();
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    public int saveFile(Long questionId, MultipartFile file) throws IOException {

        String url = "http://120.26.170.155:12138/upload";
        HttpPost httpPost = new HttpPost(url);
        // 设置请求头
        httpPost.setHeader("Accept", "application/json");

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();// 处理文件上传
        if (file != null && !file.isEmpty()) {
            // 通过 InputStreamBody 添加文件
            InputStream inputStream = file.getInputStream();
            builder.addPart("file", new InputStreamBody(inputStream, file.getOriginalFilename()));
        }
        builder.addPart("path", new StringBody(questionId.toString()));  // 添加路径字段（传递路径参数）


        // 将构建的请求体设置到 HttpPost 中
        HttpEntity entity = builder.build();
        httpPost.setEntity(entity);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpResponse response = httpClient.execute(httpPost);  // 执行请求

            // 打印响应状态码
            String strResult = EntityUtils.toString(response.getEntity());
            int statusCode = response.getStatusLine().getStatusCode();
            return statusCode;
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Question> getQuestionList(List<Long> questionId) {
        if (questionId == null) {
            throw new RuntimeException("questionId is null");
        }
        List<Question> questionList = questionMapper.selectBatchIds(questionId);
        if (questionList == null || questionList.size() == 0) {
            throw new RuntimeException("questionList is null");
        }
        return questionList;
    }
    /**
     * 获取比赛题目信息
     * @param contestId
     * @return
     */
    @Override
    public List<Question> getQuestionListById(Long contestId) {
        // 根据比赛id获取中间表
        List<ContestQuestion> questionList = contestQuestionMapper.selectList(new QueryWrapper<ContestQuestion>().eq("contest_id", contestId));
        // 从小到大排序
        Collections.sort(questionList, new Comparator<ContestQuestion>() {
            @Override
            public int compare(ContestQuestion o1, ContestQuestion o2) {
                return o2.getWeight() - o1.getWeight();
            }
        });
        // 网络流获取问题id
        List<Long> questionIdList = questionList.stream().map(ContestQuestion::getQuestionId).collect(Collectors.toList());
        // 获取题目详细
        List<Question> questions = questionMapper.selectBatchIds(questionIdList);
        return questions;
    }

    @Override
    public List<QuestionInfo> getQuestionIdListByContestId(Long contestId) {

        // 根据比赛id获取中间表
        List<ContestQuestion> questionList = contestQuestionMapper.selectList(new QueryWrapper<ContestQuestion>().eq("contest_id", contestId));
        // 从小到大排序
        Collections.sort(questionList, new Comparator<ContestQuestion>() {
            @Override
            public int compare(ContestQuestion o1, ContestQuestion o2) {
                return o2.getWeight() - o1.getWeight();
            }
        });
        // 网络流获取问题id
        List<Long> questionIdList = questionList.stream().map(ContestQuestion::getQuestionId).collect(Collectors.toList());
        // 获取题目详细
        List<Question> questions = questionMapper.selectBatchIds(questionIdList);
        List<QuestionInfo> questionInfoList = new ArrayList<>();
        for (Question question : questions) {
            QuestionInfo questionInfo = new QuestionInfo();
            questionInfo.setQuestionName(question.getTitleName());
            questionInfo.setTitledId(question.getTitleId());
            questionInfoList.add(questionInfo);
        }
        return questionInfoList;
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
    @Override
    public QuestionUserVO getQuestionVO(Question question) {
        if (question == null) {
            return null;
        }
        QuestionUserVO questionVO=new QuestionUserVO();
        BeanUtil.copyProperties(question, questionVO);
//        计算通过率
        int passPerson = question.getPassPerson();
        int tryPerson = question.getTryPerson();
        if(tryPerson==0){
            questionVO.setPassRate(0.0);
        }else{
          double passRate =  passPerson/tryPerson*1.0;
            questionVO.setPassRate(passRate);
        }

        //添加标签
//        int titleId = question.getTitleId();
//        List<String> tags = iQuestionTagService.getTagNamesBytitleId(titleId);
//        questionVO.setTags(tags);
        return questionVO;
    }

    @Override
    public Wrapper<Question> getListWrapper(QuestionFilterDTO questionFilterDTO) {
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        if (questionFilterDTO == null) {
            return queryWrapper;
        }
        Integer titleId = questionFilterDTO.getTitleId();
        String titleName = questionFilterDTO.getTitleName();
        Integer score = questionFilterDTO.getScore();
        String sortField = questionFilterDTO.getSortField();
        String sortOrder = questionFilterDTO.getSortOrder();
        Integer minScore = questionFilterDTO.getMinscore();
        Integer maxScore = questionFilterDTO.getMaxscore();
//        TODO  根据标签筛选待处理
//        List<String>  tagNames=questionFilterDTO.getTagNames();
//        if(tagNames!=null){
//            for(String tagName:tagNames){
//                List<Integer> tid=iQuestionTagService.getTitleIdsbyTagName(tagName);
//                queryWrapper.in(ObjectUtils.isNotEmpty(tid), "title_id", tid);
//            }
//        }

        queryWrapper.eq(ObjectUtils.isNotEmpty(titleId), "title_id", titleId);
        queryWrapper.like(StringUtils.isNotBlank(titleName), "title_name", titleName);
        queryWrapper.eq("status", 0);
        queryWrapper.eq(ObjectUtils.isNotEmpty(score), "score", score);
        queryWrapper.ge(ObjectUtils.isNotEmpty(minScore), "score", minScore);
        queryWrapper.le(ObjectUtils.isNotEmpty(maxScore), "score", maxScore);
        queryWrapper.eq("is_deleted", false);
        queryWrapper.orderBy(
                SqlUtils.isValidOrderBySql(sortField),
                sortOrder.equals("asc"),
                sortField
        );
        return queryWrapper;
    }

    @Override
    public Page<QuestionUserVO> getQuestionPageVO(Page<Question> questionPage) {
        List<Question> questionList = questionPage.getRecords();
        Page<QuestionUserVO> questionVOPage = new Page<>(questionPage.getCurrent(), questionPage.getSize(), questionPage.getTotal());
        if (CollectionUtils.isEmpty(questionList)) {
            return questionVOPage;
        }

        List<QuestionUserVO> questionVOList = questionList.stream().map(question -> {
            QuestionUserVO questionVO = getQuestionVO(question);
            return questionVO;
        }).collect(Collectors.toList());
        questionVOPage.setRecords(questionVOList);
        return questionVOPage;
    }

    @Override
    public void validQuestion(Question question) {
        if (question == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String titlename = question.getTitleName();
        String content = question.getContent();
        String tip = question.getTip();
        String answer = question.getAnswer();
        // 参数校验
        if (StringUtils.isNotBlank(titlename) && titlename.length() > 93) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标题过长");
        }
        if (StringUtils.isNotBlank(content) && content.length() > 2993) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
        }
        if (StringUtils.isNotBlank(tip) && tip.length() > 493) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "提示过长");
        }
        if (StringUtils.isNotBlank(answer) && answer.length() > 2993) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "答案过长");
        }
    }

    @Override
    public boolean removeQuestion(Question question) {
        int titleId = question.getTitleId();
        removeById(question.getId());
        return iQuestionTagService.removeBytitleId(titleId);
    }

}
