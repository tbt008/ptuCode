package com.example.oj.common;

import lombok.Data;

@Data
public class TestCaseResult {
    /**
     * 编译时间(ms)
     */
    private int cpu_time;
    /**
     * 运行时间(ms)
     */
    private int real_time;
    /**
     * 内存大小(byte)
     */
    private Long memory;
    private int signal;
    /**
     * return的id
      */
    private int exit_code;
    /**
     * SUCCESS = 0
     *INVALID_CONFIG = -1
     *CLONE_FAILED = -2
     *PTHREAD_FAILED = -3
     *WAIT_FAILED = -4
     *ROOT_REQUIRED = -5
     *LOAD_SECCOMP_FAILED = -6
     *SETRLIMIT_FAILED = -7
     *DUP2_FAILED = -8
     *SETUID_FAILED = -9
     *EXECVE_FAILED = -10
     *SPJ_ERROR = -11
     * CE = -12
     */
    private int error;

    /**
     * WRONG_ANSWER = -1 (this means the process exited normally, but the answer is wrong)
     * SUCCESS = 0 (this means the answer is accepted)
     * CPU_TIME_LIMIT_EXCEEDED = 1
     * REAL_TIME_LIMIT_EXCEEDED = 2
     * MEMORY_LIMIT_EXCEEDED = 3
     * RUNTIME_ERROR = 4
     * SYSTEM_ERROR = 5
     */
    private int result;

    /**
     * 跑的测试样例的文件名
     */
    private String test_case;

    /**
     * 跑的测试数据的md5加密
     */
    private String output_md5;

    /**
     * 测试数据是否输出，一般为无
     */
    private String output;
}
