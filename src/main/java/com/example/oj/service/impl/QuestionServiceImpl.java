package com.example.oj.service.impl;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.oj.common.ErrorCode;
import com.example.oj.common.Language;
import com.example.oj.domain.dto.JudgeDTO;
import com.example.oj.domain.entity.CodeRecord;
import com.example.oj.domain.entity.Question;
import com.example.oj.domain.entity.QuestionTag;
import com.example.oj.domain.entity.Tag;
import com.example.oj.domain.vo.QuestionVo;
import com.example.oj.domain.vo.ResultInfoVO;
import com.example.oj.exception.BusinessException;
import com.example.oj.mapper.QuestionMapper;
import com.example.oj.service.IQuestionService;
import com.example.oj.service.IQuestionTagService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    @Resource
    private IQuestionTagService questionTagService;

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
    @Override
    public QuestionVo getQuestionVO(Question question) {
        QuestionVo questionVO = QuestionVo.potovo(question);
        int titleId = question.getTitleId();

        List<String> tags = questionTagService.getBytitleId(titleId);
        questionVO.setTags(tags);
        return questionVO;
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
    public boolean removeByTitleid(Question question) {
        int titleId = question.getTitleId();
        removeById(question.getId());
        return questionTagService.removeBytitleId(titleId);
    }

}
