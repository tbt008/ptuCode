package com.example.oj.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.example.oj.common.PageLimit;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class QuestionFilterDTO extends PageLimit implements Serializable {

    /**
     * 题目状态 0 未提交 1 通过 2尝试过
     */

    private Integer isPass;

    /**
     * 题目id
     */
    private Integer titleId;

    /**
     * 标题
     */
    private String titleName;


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
     * 标签集合
     */

    List<String> tagNames;
}
