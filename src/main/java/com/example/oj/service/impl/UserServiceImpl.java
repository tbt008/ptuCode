package com.example.oj.service.impl;

import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.oj.common.Result;
import com.example.oj.domain.entity.User;
import com.example.oj.domain.vo.UserRegisterVO;
import com.example.oj.exception.BusinessException;
import com.example.oj.mapper.UserMapper;
import com.example.oj.service.IUserService;

import lombok.Cleanup;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author tbt
 * @since 2024-11-24
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private UserMapper userMapper;


    @Override
    public List<UserRegisterVO> registerBatch(MultipartFile file) throws IOException {

        // 将 MultipartFile 转换为 InputStream
        @Cleanup
        InputStream inputStream = file.getInputStream();

        // 创建 Workbook，判断文件类型，如果是 .xlsx 格式使用 XSSFWorkbook
        Workbook workbook = null;
        if (file.getOriginalFilename().endsWith(".xlsx")) {
            workbook = new XSSFWorkbook(inputStream);
        } else if (file.getOriginalFilename().endsWith(".xls")) {
            workbook = new HSSFWorkbook(inputStream);
        } else {
            // TODO 错误码未定
            throw new BusinessException(410, "Unsupported file format");
        }
        Sheet sheet = workbook.getSheetAt(0);
        int rowCount = sheet.getPhysicalNumberOfRows();
        List<UserRegisterVO> list = new ArrayList<UserRegisterVO>();
        List<User> userRegisterList = new ArrayList<User>();
        LocalDateTime localDateTime = LocalDateTime.now();
        for (int i = 1; i < rowCount; i++) {
            Row row = sheet.getRow(i);
            // 获取单元格并转换为字符串
            Cell idCell = row.getCell(0);
            String idString = (idCell != null) ? idCell.toString() : "";

            Cell classCell = row.getCell(1);
            String className = (classCell != null) ? classCell.toString() : "";

            Cell nameCell = row.getCell(2);
            String name = (nameCell != null) ? nameCell.toString() : "";
            // 校验字段是否完整
            if (idString == null || className == null || name == null) {
                UserRegisterVO userRegisterVO = new UserRegisterVO(idString, className, name);
                list.add(userRegisterVO);
                continue ;
            }
            // 校验id长度
            if(idString.length() != 12){
                UserRegisterVO userRegisterVO = new UserRegisterVO(idString, className, name);
                list.add(userRegisterVO);
                continue ;
            }
            //校验是否纯数字
            if(!idString.matches("[0-9]+")){
                UserRegisterVO userRegisterVO = new UserRegisterVO(idString, className, name);
                list.add(userRegisterVO);
                continue ;
            }
            Long id = Long.valueOf(idString);
            User user = userMapper.selectById(id);
            // 判断是否已经注册过
            if (user != null) {
                UserRegisterVO userRegisterVO = new UserRegisterVO(idString, className, name);
                list.add(userRegisterVO);
                continue ;
            }
            user = new User();
            user.setId(id);
            user.setClassName(className);
            user.setUserName(name);
            user.setNickName(name);
            user.setPassword(DigestUtils.md5Hex(idString));
            user.setUserType(0L);
            user.setSolveEasy(0);
            user.setSolveMiddle(0);
            user.setSolveHard(0);
            user.setScore(0);
            user.setCreateTime(localDateTime);
            user.setUpdateTime(localDateTime);
            userRegisterList.add(user);
        }
        if (userRegisterList.size() > 0 && userRegisterList != null) userMapper.saveBatch(userRegisterList);

        // 返回插入不成功的
        return list;
    }
}
