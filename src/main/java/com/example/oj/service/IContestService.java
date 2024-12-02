package com.example.oj.service;

import com.example.oj.domain.entity.Contest;
import com.example.oj.domain.vo.ContestOverviewVO;

import java.util.List;

public interface IContestService {

    Contest getContestById(Long id);

    List<ContestOverviewVO> getContestOverviewVOByPageAndPageNum(int page, int pageNum);

    Integer isInvite(Long contestId);

}
