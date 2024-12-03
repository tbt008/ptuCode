package com.example.oj.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@TableName("contest_question")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ContestQuestion {


    /**
     * 比赛id
     */
    @TableField("contest_id")
    private Long contestId;

    /**
     * 问题id
     */
    @TableField("questionId")
    private Long questionId;

    /**
     * 问题权重
     */
    @TableField("weight")
    private Integer weight;
}
