package com.example.oj.common;

import lombok.Data;

@Data
public class TestCaseResult {
    private int cpu_time;
    private int real_time;
    private Long memory;
    private int signal;
    private int exit_code;
    private int error;
    private int result;
    private String test_case;
    private String output_md5;
    private String output;
}
