package com.example.oj.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.oj.domain.entity.Question;
import com.example.oj.domain.entity.QuestionTag;
import com.example.oj.domain.entity.Tag;
import com.example.oj.mapper.QuestionTagMapper;
import com.example.oj.mapper.TagMapper;
import com.example.oj.service.IQuestionService;
import com.example.oj.service.IQuestionTagService;

import com.example.oj.service.ITagService;
import net.sf.jsqlparser.statement.select.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    @Resource
    private ITagService iTagService;


    @Override
    public List<String> getBytitleId(int titleId) {
        boolean st=lambdaQuery().eq(QuestionTag::getQuestionId,titleId).exists();
        if(st==false){
            return Collections.emptyList();
        }
        List<QuestionTag> questionTags=lambdaQuery()
//                .select(QuestionTag::getTagId)
                .eq(QuestionTag::getQuestionId,titleId)
                .list();
        List<Integer> tag_ids=questionTags.stream().map(QuestionTag::getTagId).collect(Collectors.toList());
        List<Tag> tags = iTagService.listByIds(tag_ids);
        List<String> tag_names=tags.stream().map(Tag::getName).collect(Collectors.toList());
        return tag_names;
    }

    @Override
    public boolean removeBytitleId(int titleId) {
        boolean st=lambdaQuery().eq(QuestionTag::getQuestionId,titleId).exists();
        if(st==false){
            return true;
        }
        List<QuestionTag> questionTags=lambdaQuery()
                .eq(QuestionTag::getQuestionId,titleId)
                .list();
        List<Integer> ids=questionTags.stream().map(QuestionTag::getId).collect(Collectors.toList());
        return removeByIds(ids);
    }
}
