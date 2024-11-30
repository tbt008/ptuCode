package com.example.oj.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.oj.domain.entity.QuestionTag;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tbt
 * @since 2024-11-26
 */
@Component
public interface IQuestionTagService extends IService<QuestionTag> {

    List<String> getTagNamesBytitleId(int titleId);

    List<Integer> getTitleIdsbyTagName(String tagName);

    void savetag(Integer title_id,List<String> tagNames);

    boolean removeBytitleId(int titleId);
}
