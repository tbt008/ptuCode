package com.example.oj.interceptor;

import com.example.oj.common.BaseContext;

import com.example.oj.common.JwtClaimsConstant;
import com.example.oj.common.JwtProperties;
import com.example.oj.domain.entity.UserInfo;
import com.example.oj.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * jwt令牌校验的拦截器
 */
@Component
@Slf4j
public class JwtTokenUserInterceptor implements HandlerInterceptor {

    @Resource
    private JwtProperties jwtProperties;
    @Resource
    private RedisTemplate redisTemplate;
    /**
     * 校验jwt
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
//    HttpServletRequest 获取请求数据
//    HttpServletResponse设置响应数据
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            //当前拦截到的不是动态方法，直接放行
            return true;
        }

        //1、从请求头中获取令牌
        String token = request.getHeader(jwtProperties.getTokenName());
        System.out.println(token);
        //2、校验令牌
        try {
            log.info("jwt校验:{}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getSecretKey(), token);
            Long userId = Long.valueOf(claims.get(JwtClaimsConstant.USER_ID).toString());
            Long userType = Long.valueOf(claims.get(JwtClaimsConstant.USER_TYPE).toString());
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId(userId);
            userInfo.setUserType(userType);
//          获取redistoken 对比一下

            String redisToken  = redisTemplate.opsForValue().get("login:" + claims.get(JwtClaimsConstant.USER_ID).toString()).toString();
            if (!token.equals(redisToken)){
                return false;
            }
            BaseContext.setUserInfo(userInfo);

            //3、通过，放行
            return true;
        } catch (Exception ex) {
            //4、不通过，响应401状态码
            response.setStatus(401);
            return false;
        }
    }
}
