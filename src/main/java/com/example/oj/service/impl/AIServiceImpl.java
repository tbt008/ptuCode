package com.example.oj.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.oj.common.Result;
import com.example.oj.domain.entity.ChatHistory;
import com.example.oj.mapper.AIMapper;
import com.example.oj.service.AIService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author wqb
 * AI调用
 */
@Slf4j
@Service
public class AIServiceImpl extends ServiceImpl<AIMapper, ChatHistory> implements AIService {
    @Autowired
    private AIMapper aiMapper;
    /*
    智谱清言api
 */
    private static final String ZP_API_KEY = "62682dd39f68f6f7c11bf9e4e961d84b.GV5BuEvBO6b5DeAP";
    private static final String ZP_API_URL = "https://open.bigmodel.cn/api/paas/v4/chat/completions";

    private static final String ZP_DOMAIN = "glm-4-flash";
    /*
    最大字节65536，设置20000保险一下
     */
    private static final int MAX_CHAT_HISTORY_SIZE = 20000;

    /**
     * 讯飞星火api
     * 目前是http请求方式，多余的配置未来可以改websocket用
     */
    private static final String HOST_URL = "https://spark-api.xf-yun.com/v3.5/chat";
    private static final String XH_URL = "https://spark-api-open.xf-yun.com/v1/chat/completions";
    private static final String XH_API_token = "kyNichDhYJnUpDTNxeui:BdIWtUBLFMgahQfBiwtc";
    private static final String DOMAIN = "generalv3.5";
    private static final String APP_ID = "e81fe3f7";
    private static final String API_SECRET = "NDEwMDQyNDhkMjdjZmZlZWMzZjMyYmU5";
    private static final String API_KEY = "4e3b9550ccfcd72c628df6550fe63535";


    /**
     * 讯飞星火
     *
     * @param Id 用户id
     * @param content 用户聊天
     */
    public Result XinHuoChat(String Id, String content) {
        Id += "XingHuoFCT";
        Boolean flag = false;
        // Fetch the existing chat history from the database
        QueryWrapper<ChatHistory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", Id).eq("ai", "讯飞星火");
        ChatHistory existingChatHistory = aiMapper.selectOne(queryWrapper);

        // Initialize chat history or use existing history if found
        JSONArray chatHistoryArray = new JSONArray();

        if (existingChatHistory != null) {
            flag = true;
            LocalDateTime timestamp = existingChatHistory.getTimestamp();

            if (timestamp != null) {
                // 获取当前时间
                LocalDateTime currentTime = LocalDateTime.now();

                // 计算时间差，判断是否超过6小时
                Duration duration = Duration.between(timestamp, currentTime);
                if (duration.toHours() >= 6) {
                    // 如果超过6小时，则清空聊天记录
                    aiMapper.deleteById(existingChatHistory.getId());
                    flag = false;
                    log.info("聊天缓存超时，已清除");
                }
            }
            String chatText = existingChatHistory.getChatText();
            if (chatText.length() > MAX_CHAT_HISTORY_SIZE) {
                // 清空聊天记录
                aiMapper.deleteById(existingChatHistory.getId());
                log.info("聊天缓存过多，已清除");
                return Result.error("聊天缓存过多，已清除,请刷新重试");
            }
            if(flag)chatHistoryArray = JSONArray.parseArray(existingChatHistory.getChatText());
        }

        JSONObject userMessage = new JSONObject();
        userMessage.put("role", "user");
        userMessage.put("content", content);

        chatHistoryArray.add(userMessage);
        String botResponse = sendMessageToAPI(chatHistoryArray.toString());
        if (botResponse.startsWith("请求失败114514:")) {
            return Result.error("API请求失败，错误信息: " + botResponse.substring("请求失败114514:".length()));
        }
        JSONObject assistantMessage = new JSONObject();
        assistantMessage.put("role", "assistant");
        assistantMessage.put("content", botResponse);

        chatHistoryArray.add(assistantMessage);

        if (flag) {
            //如果上面根据id有找到数据且没超时，就修改数据
            existingChatHistory.setChatText(chatHistoryArray.toJSONString());
            existingChatHistory.setTimestamp(LocalDateTime.now());
            aiMapper.updateById(existingChatHistory);
        } else {
            //没找到就新增
            ChatHistory chatHistory = new ChatHistory();

            chatHistory.setId(Id);
            chatHistory.setAi("讯飞星火");
            chatHistory.setChatText(chatHistoryArray.toJSONString());
            chatHistory.setTimestamp(LocalDateTime.now());
            log.info("新增聊天记录");
            aiMapper.insert(chatHistory);
        }
        return Result.success(botResponse);
    }

    private String sendMessageToAPI(String chatHistory) {
        try {
            String url = XH_URL;

            String jsonData = "{\n" +
                    "    \"model\": \""+ DOMAIN +"\",\n" +
                    "    \"messages\": " + chatHistory + ",\n" +
                    "    \"stream\": false\n" +
                    "}";

            URL urlObj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer "+XH_API_token);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonData.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            StringBuilder responseBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    responseBuilder.append(line);
                }
            }

            JSONObject jsonResponse = JSONObject.parseObject(responseBuilder.toString());
            JSONArray choices = jsonResponse.getJSONArray("choices");
            JSONObject message = choices.getJSONObject(0).getJSONObject("message");
            return message.getString("content");

        } catch (Exception e) {
            e.printStackTrace();
            return "请求失败114514: " + e.getMessage(); // 返回 JSON 格式字符串
        }
    }

    /**
     * 智谱清言
     */


    @Override
    public Result ZhiPuChat(String Id, String content) {
        if (content == null || content.trim().isEmpty()) {
            return Result.error("内容不能为空");
        }
        Boolean flag = false;
        //根据id找一下，看看存不存在
        QueryWrapper<ChatHistory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", Id).eq("ai", "智谱清言");
        ChatHistory existingChatHistory = aiMapper.selectOne(queryWrapper);
        //用数组存json聊天记录（api要求）
        JSONArray chatArray = new JSONArray();

        if (existingChatHistory != null) {
            try {
                LocalDateTime timestamp = existingChatHistory.getTimestamp();
                flag = true;
                if (timestamp != null) {
                    // 获取当前时间
                    LocalDateTime currentTime = LocalDateTime.now();

                    // 计算时间差，判断是否超过6小时
                    Duration duration = Duration.between(timestamp, currentTime);
                    if (duration.toHours() >= 6) {
                        // 如果超过6小时，则清空聊天记录
                        aiMapper.deleteById(existingChatHistory.getId());  // 假设 existingChatHistory 有 id 字段
                        flag = false;
                        log.info("聊天缓存超时，已清除");
                    }
                }
                // 检查聊天记录是否过大（比如超过某个大小限制）
                String chatText = existingChatHistory.getChatText();
                if (chatText.length() > MAX_CHAT_HISTORY_SIZE) {  // 这里的 MAX_CHAT_HISTORY_SIZE 是您设置的最大长度
                    // 清空聊天记录
                    aiMapper.deleteById(existingChatHistory.getId());  // 假设 existingChatHistory 有 id 字段
                    log.info("聊天缓存过长，已清除");
                    return Result.error("聊天缓存过多，已清除,请刷新重试");
                }

                // 从数据库里拿聊天记录
                if(flag)chatArray = JSONArray.parseArray(chatText);
            } catch (Exception e) {
                return Result.error("解析聊天记录失败");
            }
        }
        //存下用户的问题
        chatArray.add(createMessage("user", content));

        String response;
        //开始调用
        try {
            response = sendRequest(chatArray.toJSONString());
        } catch (Exception e) {
            return Result.error("网络请求失败，请稍后再试");
        }
        if (response == null || response.isEmpty()) {
            return Result.error("AI 响应为空");
        }
        //将 JSON 格式的字符串解析为 JSONObject 对象
        JSONObject jsonObject = JSONObject.parseObject(response);
        if (jsonObject.containsKey("error")) {
            String errorMessage = jsonObject.getJSONObject("error").getString("message");
            return Result.error("错误: " + errorMessage);
        }

        String ZpContent = "";
        JSONArray choices = jsonObject.getJSONArray("choices");
        if (choices != null && !choices.isEmpty()) {
            JSONObject choice = choices.getJSONObject(0);
            JSONObject message = choice.getJSONObject("message");
            if (message != null) {
                ZpContent = message.getString("content");
            }
        }

        if (ZpContent.isEmpty()) {
            return Result.error("AI 响应为空");
        }
        //记录ai的回答
        chatArray.add(createMessage("assistant", ZpContent));

        if (flag) {
            //如果上面根据id有找到数据，就修改数据
            existingChatHistory.setChatText(chatArray.toJSONString());
            existingChatHistory.setTimestamp(LocalDateTime.now());
            aiMapper.updateById(existingChatHistory);
        } else {
            //没找到就新增
            ChatHistory chatHistory = new ChatHistory();
            chatHistory.setId(Id);
            chatHistory.setAi("智谱清言");
            chatHistory.setChatText(chatArray.toJSONString());
            chatHistory.setTimestamp(LocalDateTime.now());
            aiMapper.insert(chatHistory);
        }

        return Result.success(ZpContent);
    }

    private JSONObject createMessage(String role, String content) {
        JSONObject message = new JSONObject();
        message.put("role", role);
        message.put("content", content);
        return message;
    }

    /**
     *
     * @param userDialogues  聊天记录
     */

    private static String sendRequest(String userDialogues) {
        try {
            // 创建 URL 和连接
            URL url = new URL(ZP_API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // 配置请求属性
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + ZP_API_KEY);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // 请求体内容
            String requestBody = """
            {
                "model": "%s",
                "messages": %s
            }
            """.formatted(ZP_DOMAIN,userDialogues);

            // 发送请求体
            try (OutputStream os = connection.getOutputStream()) {
                os.write(requestBody.getBytes());
                os.flush();
            }

            // 获取响应
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                // 成功响应
                try (Scanner scanner = new Scanner(connection.getInputStream())) {
                    scanner.useDelimiter("\\A");
                    String response = scanner.hasNext() ? scanner.next() : "";

                    return response; // 返回 JSON 格式字符串
                }
            } else {
                // 错误响应
                try (Scanner scanner = new Scanner(connection.getErrorStream())) {
                    scanner.useDelimiter("\\A");
                    String errorResponse = scanner.hasNext() ? scanner.next() : "";
                    // 解析错误响应为 JSON 格式
                    return errorResponse; // 返回 JSON 格式字符串
                }
            }
        } catch (Exception e) {
            // 捕获所有异常并返回错误信息
            e.printStackTrace();
            //这里不能直接返回e，因为需要json格式字符串，否则解析出问题
            JSONObject errorJson = new JSONObject();
            errorJson.put("error", "请求失败: " + e.getMessage());
            return "请求失败: " + e.getMessage();
        }
    }


}
