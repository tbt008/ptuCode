package com.example.oj.domain.vo;


import lombok.Data;

import java.time.LocalDate;

@Data
public class CodeRecordVO {


    private Long id;

    /**
     * 编程语言
     */
    private Integer language;

    /**
     * 用户代码
     */
    private String code;

     /**
     * 整体状态 通过，部分通过，编译错误
     */
    private String result;

    /**
     * 判题信息（json 对象）存每个测试点的信息
     */
    private String judgeInfo;

    /**
     * 判题状态（0 - 待判题、1 - 判题中、2 - 成功、3 - 失败）
     */
    private Integer status;

    /**
     * 题目 id
     */
    private Long questionId;

    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private LocalDate createTime;

}
