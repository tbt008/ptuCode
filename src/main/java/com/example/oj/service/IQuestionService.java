package com.example.oj.service;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.oj.common.TestCaseResult;
import com.example.oj.domain.dto.JudgeDTO;
import com.example.oj.domain.dto.QuestionDTO;
import com.example.oj.domain.entity.Question;
import com.example.oj.domain.vo.QuestionVo;

import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tbt
 * @since 2024-11-24
 */
@Component
public interface IQuestionService extends IService<Question> {

    Long submitQuestion(JudgeDTO judgeDTO);

    QuestionVo getQuestionVO(Question question);

    Wrapper<Question> getListWrapper(QuestionDTO questionDTO);

    Page<QuestionVo> getQuestionPageVO(Page<Question> questionPage);

    void validQuestion(Question question);

    boolean removeQuestion(Question question);
    TestCaseResult getOutputByInput(String input, Integer language, String code) throws UnsupportedEncodingException;
}
