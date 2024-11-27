package com.example.oj.mapper;

import com.example.oj.domain.entity.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author tbt
 * @since 2024-11-26
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

}
