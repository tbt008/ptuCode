package com.example.oj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.oj.exception.BusinessException;
import com.example.oj.utils.ConvertBeanUtils;
import com.example.oj.domain.entity.Contest;
import com.example.oj.domain.entity.ContestUser;
import com.example.oj.domain.entity.User;
import com.example.oj.domain.vo.ContestOverviewVO;
import com.example.oj.mapper.ContestMapper;
import com.example.oj.mapper.ContestUserMapper;
import com.example.oj.mapper.UserMapper;
import com.example.oj.service.IContestService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Service
public class ContestServiceImpl implements IContestService {

    @Resource
    private ContestMapper contestMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private ContestUserMapper contestUserMapper;

    @Override
    public Contest getContestById(Long contestId) {

        Contest contest = contestMapper.selectById(contestId);
        // 没被邀请过不返回题目
        if (contest == null) {
            throw new BusinessException(406, "contest is error");
        }
        if (isInvite(contestId) != 0) {
            contest.setProblemId(null);
        }

        return contest;
    }

    @Override
    public List<ContestOverviewVO> getContestOverviewVOByPageAndPageNum(int page, int pageSize) {

        Page<Contest> contestPage = new Page<>(page, pageSize);

        QueryWrapper<Contest> queryWrapper = new QueryWrapper<>();
        // 按开始时间倒序排序
        queryWrapper.orderByDesc("start_time");
        // 去掉删除掉的比赛
        queryWrapper.eq("id_deleted", false);
        // 查找
        IPage<Contest> pageResult = contestMapper.selectPage(contestPage, queryWrapper);

        // 获取查询结果
        List<Contest> contests = pageResult.getRecords();

        // 获取大众信息
        List<ContestOverviewVO> contestOverviewVOS = ConvertBeanUtils.convertList(contests, ContestOverviewVO.class);
        return contestOverviewVOS;
    }

    /**
     * 判断是否被邀请
     * 0代表被可以进, 1代表未输入密码, 2代表未被邀请
     * @param contestId
     * @return
     */
    @Override
    public Integer isInvite(Long contestId) {
        //TODO 获取用户id判断能不能进
        int userId = 1;
        User user = userMapper.selectById(userId);

        Contest contest = contestMapper.selectById(contestId);

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
}
