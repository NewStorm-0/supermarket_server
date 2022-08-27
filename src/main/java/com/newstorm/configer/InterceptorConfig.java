package com.newstorm.configer;

import com.newstorm.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    JwtInterceptor jwtInterceptor;

    @Autowired
    public void setJwtInterceptor(JwtInterceptor jwtInterceptor) {
        this.jwtInterceptor = jwtInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")  // 拦截所有请求，通过判断是否有token注解决定是否需要登录
                .excludePathPatterns(   //添加不拦截路径
                        "/user/login",
                        "/user/register",
                        "/membership_level/all",
                        "/order/checkout",
                        "/administrator/login",
                        "/user_coupon/get",
                        "/coupon/all",
                        "/commodity/all",
                        "/swagger-ui/**",
                        "/v3/**",
                        "/swagger-ui.html/**",
                        "/swagger-resources/**"
                );
    }
}
