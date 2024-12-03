package com.example.oj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.oj.common.BaseContext;
import com.example.oj.common.ErrorCode;
import com.example.oj.domain.dto.CreateContestDTO;
import com.example.oj.domain.entity.*;
import com.example.oj.exception.BusinessException;
import com.example.oj.mapper.*;
import com.example.oj.utils.ConvertBeanUtils;
import com.example.oj.service.IContestService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class ContestServiceImpl implements IContestService {

    @Resource
    private ContestMapper contestMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private ContestUserMapper contestUserMapper;

    @Resource
    private QuestionMapper questionMapper;

    @Resource
    private ContestQuestionMapper contestQuestionMapper;

    /**
     * 判断是否被邀请
     * 0代表被可以进, 1代表未输入密码, 2代表未被邀请, 3代表比赛未开始, 4代表比赛结束
     * @param contestId
     * @return
     */
    @Override
    public Integer isInvite(Long contestId) {
        //TODO 获取用户id判断能不能进
        int userId = 1;
        User user = userMapper.selectById(userId);

        Contest contest = contestMapper.selectById(contestId);

        LocalDateTime localDateTime = LocalDateTime.now();
        // 当前时间早于比赛时间
        if (localDateTime.isBefore(contest.getStartTime())) {
            return 3;
        }
        // 当前时间晚于比赛时间
        if (localDateTime.isAfter(contest.getEndTime())) {
            return 4;
        }
        // 判断是否是管理员, 是管理员不防止
        if (Objects.equals(user.getUserType(), "0")) {
            return 0;
        }

        ContestUser contestUser = null;
        // 判断这个比赛是否需要邀请
        if (contest.getIsInvite()) {
            // 判断用户是否被邀请
            contestUser = contestUserMapper.getByContestIdAndUserId(contestId, user.getId());
            if (contestUser == null) {
                return 2;
            }
        }
        // 判断比赛是否有密码
        if (!StringUtils.isEmpty(contest.getPassword())) {
            // 判断用户是否输入过密码
            if (!contestUser.getIsPassword()) {
                return 1;
            }
        }
        return 0;
    }

    /**
     * 通过id获取比赛基础信息
     * @param contestId
     * @return
     */
    @Override
    public Contest getContestById(Long contestId) {
        if (isInvite(contestId) != 0) {
            throw new BusinessException(ErrorCode.PERMISSION_ERROR);
        }
        Contest contest = contestMapper.selectById(contestId);
        contest.setPassword(null);
        return contest;
    }




    /**
     * 创建比赛
     * @param createContestDTO
     */
    @Override
    @Transactional
    public void createContest(CreateContestDTO createContestDTO) {
        Contest contest = ConvertBeanUtils.convert(createContestDTO, Contest.class);
        contestMapper.insert(contest);
        contest.setCreateUser(BaseContext.getUserInfo().getUserId());
        List<Long> list = createContestDTO.getQuestionList();
        for (int i = 0; i < list.size(); i++) {
            ContestQuestion contestQuestion = new ContestQuestion();
            contestQuestion.setContestId(contest.getId());
            contestQuestion.setQuestionId(list.get(i));
            contestQuestion.setWeight(i);
            contestQuestionMapper.insert(contestQuestion);
        }
    }

    /**
     *
     */
}
