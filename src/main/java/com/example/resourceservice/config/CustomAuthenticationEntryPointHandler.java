package com.example.resourceservice.config;

import com.example.resourceservice.domain.ResultCode;
import com.example.resourceservice.domain.ResultJson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.resource.OAuth2AccessDeniedException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author TAO
 * @description: 解决匿名用户访问无权限资源时的异常处理器
 * 实际上也就是用户没有认证过,请求头中没有携带了Token，或者是resource_ids范围不够
 * @date 2021/4/8 22:43
 */
@Slf4j
@Component("customAuthenticationEntryPointHandler")
public class CustomAuthenticationEntryPointHandler extends OAuth2AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        //验证为未登陆状态会进入此方法，认证错误
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        Throwable cause = authException.getCause();
        String errorMsg = "";
        if (cause instanceof OAuth2AccessDeniedException) {
            log.warn("resource_ids范围不够");
            errorMsg = "未获得访问该资源权限";
        }  else if (cause instanceof InvalidTokenException) {
            log.warn("Token解析失败");
            errorMsg = "token解析失败";
        }else if (authException instanceof InsufficientAuthenticationException) {
            log.warn("未携带token");
            errorMsg = "未携带token";
        }
        PrintWriter printWriter = response.getWriter();
        String body = ResultJson.failure(ResultCode.UNAUTHORIZED, errorMsg).toString();
        printWriter.write(body);
        printWriter.flush();
    }

}
