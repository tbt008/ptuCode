package com.example.oj.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreateContestDTO {


    private String name; // 比赛昵称

    private Long createUser; // 创建人

    private LocalDateTime startTime; // 比赛开始时间

    private LocalDateTime endTime; // 比赛结束时间

    private String announcement; // 比赛公告

    private String password; // 比赛密码

    private LocalDateTime createTime; // 创建时间

    private Boolean isInvite; // 是否需要教师导入才能比赛

    /**
     * 封面
     */
    private String cover;

    /**
     * 问题列表
     */
    private List<Long> questionList;

    private int language;
}
