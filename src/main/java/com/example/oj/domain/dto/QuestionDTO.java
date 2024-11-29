package com.example.oj.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.example.oj.common.PageLimit;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author 
 * @since 2024-11-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QuestionDTO extends PageLimit implements Serializable {

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

    /**
     * 提示
     */
    private String tip;


    /**
     * 创建人
     */
    private Integer createUser;

    /**
     * 难度
     */
    private Integer score;

    /**
     * 查询的最小难度
     */
    private Integer minscore;

    /**
     * 查询的最大难度
     */
    private Integer maxscore;

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


    List<String> tag_names;
}
