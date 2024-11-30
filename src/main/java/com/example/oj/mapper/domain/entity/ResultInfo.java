package com.example.oj.mapper.domain.entity;

import com.example.oj.common.RunStatus;
import lombok.Data;

@Data
public class ResultInfo {

    private Integer id;
    private RunStatus description;
    private Integer runTime;
    private Integer memory;

}
