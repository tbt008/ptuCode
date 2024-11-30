package com.example.oj.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.oj.domain.entity.Type;
import com.example.oj.mapper.TypeMapper;
import com.example.oj.service.ITypeService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tbt
 * @since 2024-11-26
 */
@Service
public class TypeServiceImpl extends ServiceImpl<TypeMapper, Type> implements ITypeService {

}
