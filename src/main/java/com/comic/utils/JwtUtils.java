package com.comic.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtils {

    // 令牌过期时间：24小时（单位：毫秒）
    private static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000;

    // 签名密钥（实际项目中应放在配置文件，这里简化处理）
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // 生成令牌
    public String generateToken(String username) {
        // 令牌中的自定义数据（claims）
        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .setClaims(claims)  // 添加自定义数据
                .setSubject(username)  // 设置用户名（主题）
                .setIssuedAt(new Date())  // 签发时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))  // 过期时间
                .signWith(key)  // 签名
                .compact();  // 生成令牌字符串
    }

    // 从令牌中获取用户名
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 验证令牌是否有效（用户名匹配且未过期）
    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    // 提取令牌中的声明（通用方法）
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // 提取所有声明
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 检查令牌是否过期
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // 提取过期时间
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
