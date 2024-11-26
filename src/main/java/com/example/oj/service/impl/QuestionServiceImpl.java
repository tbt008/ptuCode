package com.example.oj.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.oj.common.ErrorCode;
import com.example.oj.common.Language;
import com.example.oj.domain.dto.JudgeDTO;
import com.example.oj.domain.entity.CodeRecord;
import com.example.oj.domain.entity.Question;
import com.example.oj.domain.vo.ResultInfoVO;
import com.example.oj.exception.BusinessException;
import com.example.oj.mapper.QuestionMapper;
import com.example.oj.service.IQuestionService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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

    @Override
    public List<ResultInfoVO> submitQuestion(JudgeDTO judgeDTO) {
//        校验语言是否正常
        Integer language = judgeDTO.getLanguage();
       if(!Language.judgeById(language)){
           throw new BusinessException(ErrorCode.PARAMS_ERROR);
       }

//        查找题目是否存在

//         存在的话，获取一下题目的限制条件

//    新增一条提交记录
        CodeRecord codeRecord = new CodeRecord();
       codeRecord.setCode(judgeDTO.getCode());
       codeRecord.setLanguage(language);
       codeRecord.setQuestionId(judgeDTO.getQuestionId());
//     TODO  登录后获取用户id
       codeRecord.setUserId(1L);

        codeRecord.setCreateTime(LocalDateTime.now());
//        状态待判题
        codeRecord.setStatus(0);

//        判题机跑代码


//        获取结果和响应
//        codeRecord.setResult()
//        codeRecord.setJudgeInfo()


//        题目的提交数+1  如果通过了通过数+1

//        封装响应
        return null;
    }
}
