package com.example.oj.domain.vo;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.oj.domain.entity.Question;
import com.example.oj.domain.entity.Tag;
import com.example.oj.service.impl.QuestionServiceImpl;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
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
     * 判断题目的测试用例
     */
    @TableField("judge_case")
    private String judgeCase;

    /**
     * 时间限制
     */
    private Integer timeLimit;

    /**
     * 空间限制
     */
    private Integer memoryLimit;

    List<String> tags;

    /**
     *
     * @param question
     * @return
     */
    public static QuestionVo potovo(Question question) {
        if (question == null) {return null;}
        QuestionVo questionVO = new QuestionVo();
        BeanUtils.copyProperties(question, questionVO);
        return questionVO;
    }

}
