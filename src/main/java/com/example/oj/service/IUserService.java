package com.example.oj.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.oj.domain.entity.User;
import com.example.oj.domain.vo.UserRegisterVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author tbt
 * @since 2024-11-24
 */
public interface IUserService  extends IService<User> {

    List<UserRegisterVO> registerBatch(MultipartFile file) throws IOException;
}
