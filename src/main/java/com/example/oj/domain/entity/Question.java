package com.example.oj.domain.entity;

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
 * @author 
 * @since 2024-11-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("question")
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 题目id
     */
    private Integer titleId;

    /**
     * 标题
     */
    private String titleName;

    /**
     * 状态 0正常 1比赛 后续待补充
     */
    private Integer status;

    /**
     * 内容
     */
    private String content;

    /**
     * 示例
     */
    private String example;

    private LocalDateTime createTime;

    /**
     * 提示
     */
    private String tip;

    private LocalDateTime updateTime;

    private Integer isDeleted;

    /**
     * 创建人
     */
    private Integer createUser;

    /**
     * 难度
     */
    private Integer score;

    /**
     * 尝试过的人数
     */
    private Integer tryPerson;

    /**
     * 通过的人数
     */
    private Integer passPerson;

    /**
     * 判断题目的测试用例
     */
    private String judgeCase;

    /**
     * 答案
     */
    private String answer;

    /**
     * 时间限制
     */
    private Integer timeLimit;

    /**
     * 空间限制
     */
    private Integer memoryLimit;


}
