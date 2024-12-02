package com.example.oj.aop;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.oj.annotation.AuthCheck;
import com.example.oj.common.ErrorCode;
import com.example.oj.common.Permission;
import com.example.oj.exception.BusinessException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class AuthAspect {
    @Around("@annotation(authCheck)")
    public Object doAuthCheck(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {

        Permission permission = authCheck.permission();

        boolean auth = Permission.CheckAuth(permission);
        if(auth==false){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        // 通过权限校验，放行
        return joinPoint.proceed();
    }
}
