package com.eddy.evaluate.config;


import com.eddy.evaluate.annotation.PassToken;
import com.eddy.evaluate.entity.SysUser;
import com.eddy.evaluate.exception.InvalidParamsException;
import com.eddy.evaluate.exception.LoginFailureException;
import com.eddy.evaluate.util.ContextUtil;
import com.eddy.evaluate.util.JwtHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;


public class TokenInterceptor implements HandlerInterceptor {

    private JwtHelper jwtHelper;

    private JwtConfiguration configuration;

    public TokenInterceptor() {
        jwtHelper = SpringContextUtils.getBean(JwtHelper.class);
        configuration = SpringContextUtils.getBean(JwtConfiguration.class);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

        String token = request.getHeader(configuration.getHead());

        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        //检查是否有PassToken注释，有则跳过认证
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }

        if (StringUtils.isBlank(token)) {
            throw new InvalidParamsException("Token为空验证错误");
        }

        SysUser sysUser = jwtHelper.validateTokenAndGetClaims(token);
        if (sysUser == null) {
            throw new LoginFailureException("用户不存在，请重新登录");
        } else {
            ContextUtil.setUser(sysUser);
        }

        return true;
    }
}
