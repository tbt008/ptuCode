package com.example.oj.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.example.oj.domain.entity.Question;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author tbt
 * @since 2024-11-24
 */
@Data
public class QuestionVo implements Serializable {

    /**
     * 题目id
     */
    @TableField("title_id")
    private Integer titleId;

    /**
     * 标题
     */
    @TableField("title_name")
    private String titleName;


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
    private Integer createUser;

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
     * 时间限制
     */
    private Integer timeLimit;

    /**
     * 空间限制
     */
    private Integer memoryLimit;

    List<String> tags;



}
