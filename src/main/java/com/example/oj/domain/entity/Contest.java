package com.example.oj.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@TableName("contest")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Contest {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id; // 主键

    @TableField(value = "name")
    private String name; // 比赛昵称

    @TableField(value = "create_user")
    private Long createUser; // 创建人

    @TableField(value = "start_time")
    private LocalDateTime startTime; // 比赛开始时间

    @TableField(value = "end_time")
    private LocalDateTime endTime; // 比赛结束时间


    @TableField(value = "announcement")
    private String announcement; // 比赛公告

    @TableField(value = "password")
    private String password; // 比赛密码

    @TableField(value = "id_deleted")
    private Boolean idDeleted; // 是否删除

    @TableField(value = "create_time")
    private LocalDateTime createTime; // 创建时间

    @TableField(value = "is_invite")
    private Boolean isInvite; // 是否需要教师导入才能比赛

    @TableField(value = "cover")
    private String cover;

    @TableField(value = "language")
    private Integer language;
}
