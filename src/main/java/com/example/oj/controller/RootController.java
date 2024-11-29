package com.example.oj.controller;

import com.example.oj.common.PageLimit;
import com.example.oj.common.Result;
import com.example.oj.domain.dto.UserRegisterDTO;
import org.springframework.web.bind.annotation.*;
//管理员权限
@RestController
@RequestMapping("/root")
public class RootController {

    /**
     * 根据id查找用户所有信息
     */
    @GetMapping("/get/{id}")
    public Result getUserById(@PathVariable("id") Long id){
        return null;
    }
    /**
     * 根据id设置用户停用
     */
    @GetMapping("/stop/{id}")
    public Result stopUserById(@PathVariable("id") Long id){
        return null;
    }

    /**
     * 删除用户
     */
    @PostMapping("/delete/{id}")
    public Result deleteUserById(@PathVariable("id") Long id ) {
      return null;
    }

    /**
     * 更新用户
     */
    @PostMapping("/update")

    public Result updateUser(@RequestBody UserRegisterDTO userRegisterDTO){
      return null;
    }


    /**
     * 分页获取用户列表
     */
    @PostMapping("/list/page")
    public Result listUserByPage(@RequestBody PageLimit pageLimit){
     return null;
    }



}
