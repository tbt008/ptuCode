package com.example.oj.controller;


import com.example.oj.common.ErrorCode;
import com.example.oj.common.Result;
import com.example.oj.domain.dto.UserLoginDTO;
import com.example.oj.domain.dto.UserRegisterDTO;
import com.example.oj.domain.entity.User;
import com.example.oj.exception.BusinessException;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

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
//    根据id查找用户（不包含敏感信息）
    @GetMapping("/get/{id}")
    public Result getUserById(){
        return null;
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result userRegister(@RequestBody UserRegisterDTO userRegisterDTO) {
//要对密码md5加密  账号要限制长度和数字  邮箱必填
              return null;
    }
    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result userLogin(@RequestBody UserLoginDTO userLoginDTO) {

//        生成jwt+redis存jwt
        return null;
    }

    /**
     * 用户注销
     */
    @PostMapping("/logout")
    public Result userLogout() {
//        退出
//        删除redis中的token使登录无效

     return null;
    }

    /**
     * 获取当前登录用户
     */
    @GetMapping("/get/login")
    public Result getLoginUser() {
      return null;
    }

    /**
     * 更新个人信息
     */
    @PostMapping("/update/my")
    public Result updateMyUser(@RequestBody User user){
        return null;
    }
}
