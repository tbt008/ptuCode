package com.example.oj.controller;


import cn.hutool.json.JSONUtil;

import com.example.oj.common.*;
import com.example.oj.domain.dto.UserLoginDTO;
import com.example.oj.domain.dto.UserRegisterDTO;
import com.example.oj.domain.entity.User;
import com.example.oj.domain.entity.UserInfo;
import com.example.oj.domain.vo.UserRegisterVO;
import com.example.oj.exception.BusinessException;
import com.example.oj.service.IUserService;
import com.example.oj.utils.JwtUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 普通用户表 前端控制器
 * </p>
 *
 * @author tbt
 * @since 2024-11-24
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private JwtProperties jwtProperties;

    // 根据id查找用户（不包含敏感信息）
    @GetMapping("/get/{id}")
    public Result getUserById(@PathVariable Long id) {
        if (id == null) {
            return Result.error(400, "用户不存在");
        }
        User user = userService.getById(id);
        if (user == null) {
            return Result.error(400, "用户不存在");
        }
        user.setPassword("**********");
        return Result.success(user);
    }


    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result userRegister(@RequestBody UserRegisterDTO userRegisterDTO) {
        // TODO 要求手动开放
        //要对密码md5加密  账号要限制长度和数字
        if (userRegisterDTO.getId().length() != 12) {
            throw new BusinessException(400, "账号不符合规范");
        }
        if (userRegisterDTO.getId() == null || userRegisterDTO.getPassword() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //校验是否纯数字
        if (!userRegisterDTO.getId().matches("[0-9]+")) {
            throw new BusinessException(400, "账号不符合规范");
        }

        User user = new User();
        user.setId(Long.valueOf(userRegisterDTO.getId()));
        user.setPassword(DigestUtils.md5Hex(userRegisterDTO.getPassword()));
        boolean isSuccess = userService.save(user);
        if (isSuccess) {
            return Result.success("注册成功！");
        } else {
            return Result.error(400, "注册失败！");
        }
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result userLogin(@RequestBody UserLoginDTO userLoginDTO) {
        System.out.println(userLoginDTO);
        if (userLoginDTO.getId() == null || userLoginDTO.getPassword() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (userLoginDTO.getId().length() != 12) {
            throw new BusinessException(400, "账号不符合规范");
        }
        //校验是否纯数字
        if (!userLoginDTO.getId().matches("[0-9]+")) {
            throw new BusinessException(400, "账号不符合规范");
        }
        String s = DigestUtils.md5Hex(userLoginDTO.getPassword());
        //查询用户是否存在
        User user = userService.lambdaQuery().eq(User::getId, userLoginDTO.getId()).eq(User::getPassword, s).one();
        if (user == null) {
            throw new BusinessException(400, "用户已存在");
        }

//        生成jwt
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getId());
        claims.put(JwtClaimsConstant.USER_TYPE, user.getUserType());
        String token = JwtUtil.createJWT(jwtProperties.getSecretKey(), jwtProperties.getTtl(), claims);
//        存redis
//        先删除旧的token，就是说会把上一个登录同号的人挤下线
        redisTemplate.opsForValue().getAndDelete("login:"+userLoginDTO.getId());
        redisTemplate.opsForValue().set("login:"+userLoginDTO.getId(),token,jwtProperties.getTtl());
        return Result.success(token);
    }

    /**
     * 用户注销
     */
    @PostMapping("/logout")
    public Result userLogout() {
        // 退出
        // 删除redis中的token使登录无效
        UserInfo user = BaseContext.getUserInfo();
        redisTemplate.opsForValue().getAndDelete("login:"+user.getUserId().toString());
        return Result.success("退出成功");
    }

    /**
     * 获取当前登录用户
     */
    @GetMapping("/get/user")
    public Result getLoginUser() {
        UserInfo userInfo = BaseContext.getUserInfo();
        User user = userService.getById(userInfo.getUserId());
        // 去掉敏感信息
        user.setPassword("");
        user.setUserType("");
        return Result.success(user);
    }

    /**
     * 更新个人信息
     */
    @PostMapping("/update/my")
    public Result updateMyUser(@RequestBody User user){
        return null;
    }
}
