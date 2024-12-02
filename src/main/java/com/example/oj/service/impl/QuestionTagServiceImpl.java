package com.example.oj.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.oj.domain.entity.QuestionTag;
import com.example.oj.domain.entity.Tag;
import com.example.oj.mapper.QuestionTagMapper;
import com.example.oj.service.IQuestionTagService;

import com.example.oj.service.ITagService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
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
    public List<String> getTagNamesBytitleId(int titleId) {
        boolean st=lambdaQuery().eq(QuestionTag::getQuestionId,titleId).exists();
        if(st==false){
            return Collections.emptyList();
        }
        List<QuestionTag> questionTags=lambdaQuery()
                .eq(QuestionTag::getQuestionId,titleId)
                .list();
        List<Long> tag_ids=questionTags.stream().map(QuestionTag::getTagId).collect(Collectors.toList());
        List<Tag> tags = iTagService.listByIds(tag_ids);
        List<String> tag_names=tags.stream().map(Tag::getName).collect(Collectors.toList());
        return tag_names;
    }

    @Override
    public List<Integer> getTitleIdsbyTagName(String tagName) {
        Tag tag = iTagService.lambdaQuery().eq(Tag::getName, tagName).one();
        if(tag==null){
            return null;
        }
        Integer id=Integer.valueOf(String.valueOf(tag.getId()));
        List<Integer> title_ids = listObjs(new LambdaQueryWrapper<QuestionTag>()
                        .select(QuestionTag::getQuestionId)
                        .eq(QuestionTag::getTagId, id)
                , obj -> Integer.valueOf(obj.toString())
        );
        if(title_ids==null){
            return null;
        }
        return title_ids;
    }

    @Override
    public void savetag(Integer title_id,List<String> tagNames) {
        List<Integer> ids = iTagService.listObjs(new LambdaQueryWrapper<Tag>()
                        .select(Tag::getId)
                        .in(Tag::getName, tagNames)
                , obj -> Integer.valueOf(obj.toString())
        );
        if(ids!=null){
            HashSet<Integer> set=new HashSet<>(ids);
            List<QuestionTag> questionTags = new ArrayList<>();
            for(Integer tag_id:set){
                questionTags.add(new QuestionTag().setTagId(tag_id).setQuestionId(title_id));
            }
            saveBatch(questionTags);
        }
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
