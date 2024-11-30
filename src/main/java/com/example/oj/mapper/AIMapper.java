package com.example.oj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.oj.domain.entity.ChatHistory;
import org.apache.ibatis.annotations.Mapper;
import com.example.oj.domain.dto.ChatDTO;

@Mapper
public interface AIMapper extends BaseMapper<ChatHistory> {

}
