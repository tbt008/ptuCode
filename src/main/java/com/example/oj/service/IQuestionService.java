package com.example.oj.service;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.oj.common.TestCaseResult;
import com.example.oj.domain.dto.JudgeDTO;
import com.example.oj.domain.dto.QuestionFilterDTO;
import com.example.oj.domain.entity.Question;
import com.example.oj.domain.vo.QuestionUserVO;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tbt
 * @since 2024-11-24
 */
public interface IQuestionService extends IService<Question> {

    Long submitQuestion(JudgeDTO judgeDTO);

    QuestionUserVO getQuestionVO(Question question);

    Wrapper<Question> getListWrapper(QuestionFilterDTO questionFilterDTO);

    Page<QuestionUserVO> getQuestionPageVO(Page<Question> questionPage);

    void validQuestion(Question question);

    boolean removeQuestion(Question question);
    TestCaseResult getOutputByInput(String input, Integer language, String code) throws UnsupportedEncodingException;

    int saveFile(Long questionId, MultipartFile file) throws IOException;

    List<Question> getQuestionList(List<Long> questionId);
}
