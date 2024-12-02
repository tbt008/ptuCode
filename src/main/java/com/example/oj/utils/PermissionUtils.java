package com.example.oj.utils;

import com.example.oj.domain.entity.CodeRecord;
import com.example.oj.domain.entity.Contest;
import com.example.oj.domain.entity.Question;
import com.example.oj.mapper.CodeRecordMapper;
import com.example.oj.mapper.ContestMapper;
import com.example.oj.mapper.QuestionMapper;
import com.example.oj.mapper.UserMapper;

import javax.annotation.Resource;

public class PermissionUtils {

    @Resource
    private UserMapper userMapper;

    @Resource
    private QuestionMapper questionMapper;

    @Resource
    private ContestMapper contestMapper;

    @Resource
    private CodeRecordMapper codeRecordMapper;

    /**
     * 具体哪个题目的控制权
     * @param problemId
     * @return
     */
    public boolean problemController(Long problemId) {
        // TODO userId
        Long userId = 202211404217L;
        Long userType = userMapper.selectById(userId).getUserType();
        if ((userType & 1L) != 0L) {
            return true;
        }
        Question problem = questionMapper.selectById(problemId);
        if (problem.getCreateUser() == userId) {
            return true;
        }
        return false;
    }

    /**
     * 对添加题目的控制
     * @return
     */
    public boolean problemController() {
        // TODO userId
        Long userId = 202211404217L;
        Long userType = userMapper.selectById(userId).getUserType();
        if ((userType & 1L) != 0L) {
            return true;
        }
        if (((userType & (1L << 1L)) != 0L)) {
            return true;
        }
        return false;
    }

    /**
     * 具体哪个比赛的控制权
     * @param contestId
     * @return
     */
    public boolean contestController(Long contestId) {
        // TODO userId
        Long userId = 202211404217L;
        Long userType = userMapper.selectById(userId).getUserType();
        if ((userType & 1L) != 0L) {
            return true;
        }
        Contest contest = contestMapper.selectById(contestId);
        if (contest.getCreateUser() == userId) {
            return true;
        }
        return false;
    }

    /**
     * 对添加比赛的控制
     * @return
     */
    public boolean contestController() {
        // TODO userId
        Long userId = 202211404217L;
        Long userType = userMapper.selectById(userId).getUserType();
        if ((userType & 1L) != 0L) {
            return true;
        }
        if (((userType & (1L << 2L)) != 0L)) {
            return true;
        }
        return false;
    }


    public boolean UserController() {
        // TODO userId
        Long userId = 202211404217L;
        Long userType = userMapper.selectById(userId).getUserType();
        if ((userType & 1L) != 0L) {
            return true;
        }
        if ((userType & (1L << 3L)) != 0) {
            return true;
        }
        return false;
    }

    /**
     * 具体源代码控制权, 以及查重
     * @param codeRecordId
     * @return
     */
    public boolean sourceBrowser(Long codeRecordId) {
        // TODO userId
        Long userId = 202211404217L;
        Long userType = userMapper.selectById(userId).getUserType();
        if ((userType & 1L) != 0L) {
            return true;
        }
        if ((userType & (1L << 4L)) != 0) {
            return true;
        }
        CodeRecord codeRecord = codeRecordMapper.selectById(codeRecordId);
        if (codeRecord.getUserId() == userId) {
            // TODO 考试时间以前交的也不让查看
            return true;
        }
        return false;
    }

    public boolean announcement() {
        // TODO userId
        Long userId = 202211404217L;
        Long userType = userMapper.selectById(userId).getUserType();
        if ((userType & 1L) != 0L) {
            return true;
        }
        if ((userType & (1L << 5L)) != 0L) {
            return true;
        }
        return false;
    }

    public boolean discussController() {
        // TODO userId
        Long userId = 202211404217L;
        Long userType = userMapper.selectById(userId).getUserType();
        if ((userType & 1L) != 0L) {
            return true;
        }
        if ((userType & (1L << 6L)) != 0L) {
            return true;
        }
        return false;
    }

}
