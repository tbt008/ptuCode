package com.example.oj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.oj.domain.entity.ContestUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ContestUserMapper extends BaseMapper<ContestUser> {

    @Select("select from where contest = #{contestId} and user_id = #{userId}")
    ContestUser getByContestIdAndUserId(Integer contestId, Long userId);
}
