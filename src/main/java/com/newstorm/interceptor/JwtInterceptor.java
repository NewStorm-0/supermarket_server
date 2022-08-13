package com.newstorm.interceptor;

import com.auth0.jwt.exceptions.*;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.newstorm.common.JwtUtils;
import com.newstorm.exception.BaseException;
import com.newstorm.exception.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String jwt = request.getHeader(JwtUtils.AUTH_HEADER_KEY);
        String token = jwt.substring(JwtUtils.TOKEN_PREFIX.length());
        log.info(jwt);
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        // 执行认证
        if (StringUtils.isEmpty(token)) {
            throw new JwtException("token为空，请重新登录");
        }
        // KEY加签验证 token
        try {
            DecodedJWT decodedJWT = JwtUtils.verify(token);
            System.out.println("The token will expire at " + decodedJWT.getExpiresAt());
        } catch (SignatureVerificationException e) {
            throw new JwtException("无效签名！");
        } catch (TokenExpiredException e) {
            throw new JwtException("token过期，请重新登录");
        } catch (AlgorithmMismatchException e) {
            throw new JwtException("算法不一致");
        } catch (JWTDecodeException e) {
            throw new JwtException("token格式错误，请重新登录");
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw new BaseException(e.getMessage());
        }
        return true;
    }
}
