package com.example.oj.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.oj.common.TestCaseResult;
import com.example.oj.domain.entity.CodeRecord;
import com.example.oj.domain.vo.CodeRecordVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tbt
 * @since 2024-11-26
 */
public interface ICodeRecordService extends IService<CodeRecord> {


    CodeRecord codeRecordGetById(Long id);


    List<TestCaseResult> getTestCaseResultListBySubmissionId(Long submissionId);

    List<CodeRecordVO> getRecordByUid(Long id);
}
