package com.example.oj.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author tbt
 * @since 2024-11-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户名
     */
    @TableField("user_name")
    private String userName;

    /**
     * 昵称
     */
    @TableField("nick_name")
    private String nickName;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 用户个人简介（不得超过25字， 默认为该用户啥也没有写）
     */
    @TableField("description")
    private String description;

    /**
     * 账号状态（0正常 1停用）
     */
    @TableField("status")
    private String status;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 手机号
     */
    @TableField("phone_number")
    private String phoneNumber;

    /**
     * 用户性别（0男，1女，2未知）
     */
    @TableField("sex")
    private String sex;

    /**
     * 头像
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 用户类型（0管理员，1普通用户）
     */
    @TableField("user_type")
    private String userType;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

/**
 * 解决简单问题的个数
 */
  @TableField("solve_easy")
    private Integer solveEasy;

    /**
     * 解决中等问题的个数
     */
  @TableField("solve_middle")
    private Integer solveMiddle;
    /**
     * 解决困难问题的个数
     */
  @TableField("solve_hard")
    private Integer solveHard;
    /**
     * 分数 （简单+1分，中等+3分，困难+5分）
     */
    @TableField("score")
    private Integer score;


    /**
     * 删除标志（0代表未删除，1代表已删除）
     */
    @TableField("is_deleted")
    private Integer isDeleted;

    @TableField("class_name")
    private String className;


}
