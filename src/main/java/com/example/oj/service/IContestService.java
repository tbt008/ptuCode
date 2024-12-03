package com.example.oj.service;

import com.example.oj.domain.dto.CreateContestDTO;
import com.example.oj.domain.entity.Contest;
import com.example.oj.domain.entity.Question;
import com.example.oj.domain.vo.ContestVO;

import java.util.List;

public interface IContestService {

    Integer isInvite(Long contestId);

    Contest getContestById(Long contestId);

    List<Question> getQuestionListById(Long contestId);

    void createContest(CreateContestDTO createContestDTO);
}
