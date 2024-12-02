package com.example.oj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.oj.domain.entity.Contest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ContestMapper extends BaseMapper<Contest> {

}
