package com.example.oj.mapper.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2024-11-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("article")
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文章的id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 创建用户id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 标题名称
     */
    @TableField("title")
    private String title;

    /**
     * 点赞个数
     */
    @TableField("like_num")
    private Integer likeNum;

    /**
     * 收藏个数
     */
    @TableField("favour_num")
    private Integer favourNum;

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
     * 是否删除
     */
    @TableField("is_delete")
    private Integer isDelete;

    /**
     * 文章内容
     */
    @TableField("content")
    private String content;

    /**
     * 文章类别(0 表示 算法文章， 1表示题解文章，2 表示经验分享文章，3表示杂谈文章， 4表示竞赛文章，5表示算法板子文章)
     */
    @TableField("article_type")
    private Integer articleType;

    /**
     * 文章的阅读个数
     */
    @TableField("article_reads")
    private Integer articleReads;

    /**
     * 文章来源id
     */
    @TableField("source_id")
    private Long sourceId;


}
