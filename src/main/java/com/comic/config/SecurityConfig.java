package com.comic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 关闭CSRF保护
                .csrf(csrf -> csrf.disable())
                // 禁用表单登录和HTTP基本认证
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                // 配置URL访问权限（使用最新语法）
                .authorizeHttpRequests(auth -> auth
                        // 允许所有OPTIONS请求
                        .requestMatchers("/**").permitAll()
                        // 允许POST请求访问注册接口
                        .requestMatchers("/api/user/register").permitAll()
                        // 允许GET请求访问测试接口
                        .requestMatchers("/hello").permitAll()
                        // 其他所有请求需要认证
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
