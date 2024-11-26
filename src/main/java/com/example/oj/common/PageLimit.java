package com.example.oj.common;

import lombok.Data;

@Data
public class PageLimit {
      /**
     * 当前页号
     */
    private long pageStart  = 1;

    /**
     * 页面大小
     */
    private long pageSize = 10;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序顺序（默认升序）asc desc
     */
    private String sortOrder = "asc";
}
