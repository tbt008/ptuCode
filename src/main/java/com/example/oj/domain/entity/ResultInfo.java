package com.example.oj.domain.entity;

import com.example.oj.common.RunStatus;
import lombok.Data;

@Data
public class ResultInfo {

    private Long id;
    private RunStatus description;
    private Integer runTime;
    private Integer memory;

}
