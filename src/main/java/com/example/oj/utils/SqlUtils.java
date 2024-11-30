package com.example.oj.utils;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.oj.common.ErrorCode;
import com.example.oj.exception.BusinessException;

public class SqlUtils {

    public static String SQL_PATTERN = "[a-zA-Z0-9_\\ \\,\\.]+";
    /**
     * 仅支持字母、数字、下划线、空格、逗号、小数点
     * @param sortField
     * @return
     */
    public static boolean isValidOrderBySql(String sortField) {
        if (StringUtils.isBlank(sortField)){
            return false;
        }
        Boolean st=sortField.matches(SQL_PATTERN);
        if(st==false){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"查询参数错误");
        }
        return true;
    }
}
