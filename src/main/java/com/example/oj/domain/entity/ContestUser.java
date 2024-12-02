package com.example.oj.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@TableName("contest_user")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ContestUser {

    @TableField("contest") // 指定数据库中的字段名
    private Long contest; // 比赛id

    @TableField("user_id")
    private Long userId; // 用户id

    @TableField("is_password")
    private Boolean isPassword; // 用户是否输入过密码
}
