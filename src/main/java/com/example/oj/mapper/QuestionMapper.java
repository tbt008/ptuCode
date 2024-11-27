package com.example.oj.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.oj.domain.entity.Question;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author tbt
 * @since 2024-11-24
 */
@Mapper
public interface QuestionMapper extends BaseMapper<Question> {

}
