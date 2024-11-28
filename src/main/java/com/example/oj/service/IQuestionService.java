package com.example.oj.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.oj.domain.dto.JudgeDTO;
import com.example.oj.domain.entity.Question;
import com.example.oj.domain.vo.QuestionVo;
import com.example.oj.domain.vo.ResultInfoVO;
import org.springframework.stereotype.Component;

import java.util.List;

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

    List<ResultInfoVO> submitQuestion(JudgeDTO judgeDTO);

    QuestionVo getQuestionVO(Question question);

    void validQuestion(Question question);

    boolean removeByTitleid(Question question);
}
