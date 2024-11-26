package com.example.oj.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.oj.domain.entity.QuestionTag;
import com.example.oj.mapper.QuestionTagMapper;
import com.example.oj.service.IQuestionTagService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tbt
 * @since 2024-11-26
 */
@Service
public class QuestionTagServiceImpl extends ServiceImpl<QuestionTagMapper, QuestionTag> implements IQuestionTagService {

}
