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
 * 
 * </p>
 *
 * @author tbt
 * @since 2024-11-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("question")
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

//雪花
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 题目id
     */
//    自增
    @TableField("title_id")
    private Integer titleId;

    /**
     * 标题
     */
    @TableField("title_name")
    private String titleName;

    /**
     * 状态 0正常 1比赛 后续待补充
     */
    @TableField("status")
    private Integer status;

    /**
     * 内容
     */
    @TableField("content")
    private String content;

    /**
     * 示例
     */
    @TableField("example")
    private String example;

    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 提示
     */
    @TableField("tip")
    private String tip;

    @TableField("update_time")
    private LocalDateTime updateTime;

    @TableField("is_deleted")
    private Integer isDeleted;

    /**
     * 创建人
     */
    @TableField("create_user")
    private Long createUser;

    /**
     * 难度
     */
    @TableField("score")
    private Integer score;

    /**
     * 尝试过的人数
     */
    @TableField("try_person")
    private Integer tryPerson;

    /**
     * 通过的人数
     */
    @TableField("pass_person")
    private Integer passPerson;

    /**
     * 答案
     */
    @TableField("answer")
    private String answer;

    /**
     * 时间限制(单位s)
     */
    @TableField("time_limit")
    private Integer timeLimit;

    /**
     * 空间限制(单位mb)
     */
    @TableField("memory_limit")
    private Integer memoryLimit;

}
