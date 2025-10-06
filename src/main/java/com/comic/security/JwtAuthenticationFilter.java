package com.comic.security;

import com.comic.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.annotation.Resource;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Resource
    private JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        // 1. 从请求头中获取令牌（格式：Bearer token）
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);  // 截取"Bearer "后面的令牌部分
            try {
                username = jwtUtils.extractUsername(token);  // 从令牌中获取用户名
            } catch (Exception e) {
                logger.error("令牌解析失败: " + e.getMessage());
            }
        }

        // 2. 验证令牌并设置认证信息
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 令牌有效且用户未被认证
            if (jwtUtils.validateToken(token, username)) {
                // 创建认证令牌（实际项目中可从数据库查询用户权限）
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        username, null, null
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 将认证信息存入上下文（后续接口可通过SecurityContext获取用户信息）
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 3. 继续执行过滤链
        filterChain.doFilter(request, response);
    }
}
