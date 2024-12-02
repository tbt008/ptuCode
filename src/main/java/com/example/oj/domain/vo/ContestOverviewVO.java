package com.example.oj.domain.vo;


import java.time.LocalDateTime;

/**
 * 比赛概况
 */
public class ContestOverviewVO {

    private int id; // 主键

    private String name; // 比赛昵称

    private int createUser; // 创建人

    private LocalDateTime startTime; // 比赛开始时间

    private LocalDateTime endTime; // 比赛结束时间

    private String password; // 比赛密码

    private Boolean idDeleted; // 是否删除

    private LocalDateTime createTime; // 创建时间

    private Boolean isInvite; // 是否需要教师导入才能比赛
}
