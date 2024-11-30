package com.example.oj.service.impl;

import com.example.oj.mapper.domain.entity.Tag;
import com.example.oj.mapper.TagMapper;
import com.example.oj.service.ITagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 
 * @since 2024-11-28
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements ITagService {

}
