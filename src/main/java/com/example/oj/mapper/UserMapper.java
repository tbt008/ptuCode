package com.example.oj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.oj.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author tbt
 * @since 2024-11-24
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
