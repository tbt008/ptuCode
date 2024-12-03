package com.example.oj.domain.vo;

import com.example.oj.domain.entity.QuestionInfo;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ContestVO {

    private Long id; // 主键

    private String name; // 比赛昵称

    private LocalDateTime startTime; // 比赛开始时间

    private LocalDateTime endTime; // 比赛结束时间

    private String announcement; // 比赛公告

    private String password; // 比赛密码

    private Boolean idDeleted; // 是否删除

    private LocalDateTime createTime; // 创建时间

    private Boolean isInvite; // 是否需要教师导入才能比赛

    private String cover;

    private Integer language;

    private List<QuestionInfo> questionInfoList;

}
