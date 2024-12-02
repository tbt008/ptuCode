package com.example.oj.controller;

import com.example.oj.common.PageLimit;
import com.example.oj.common.Result;
import com.example.oj.domain.dto.UserRegisterDTO;
import com.example.oj.domain.vo.UserRegisterVO;
import com.example.oj.service.IUserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

//管理员权限
@RestController
@RequestMapping("/root")
public class RootController {


    @Resource
    private IUserService userService;

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


    /**
     * 管理员excel批量导入人数
     *
     * @return
     */
    @PostMapping("/registerbatch")
    public Result registerBatch(@RequestParam("file") MultipartFile file) throws IOException {
        // TODO 判断当前操作用户是否为管理员
        List<UserRegisterVO> registerVOList = userService.registerBatch(file);
        if (registerVOList == null || registerVOList.isEmpty()) {
            return Result.success();
        }
        return Result.error(404, registerVOList.toString());
    }


}
