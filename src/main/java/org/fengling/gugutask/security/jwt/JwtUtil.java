package org.fengling.gugutask.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    // 使用 jjwt 0.11.x API 创建签名密钥
    private final SecretKey secretKey = Keys.hmacShaKeyFor("mochalifemochalifemochalifemochalife".getBytes());

    // 从token中提取用户名
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 从token中提取指定的claim
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // 解析token，获取所有的claims
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 检查token是否过期
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // 获取token的过期时间
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // 生成token
    public String generateToken(String username, Long userId, List<String> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles); // 将用户的角色信息放入token的claims中
        claims.put("userId", userId); // 将用户的ID信息放入token的claims中
        return createToken(claims, username);
    }

    // 创建token
    private String createToken(Map<String, Object> claims, String subject) {
        // 1 小时的过期时间
        long jwtExpirationMs = 1000 * 60 * 60;
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // 验证token的合法性
    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    public List<GrantedAuthority> getAuthorities(String token) {
        Claims claims = extractAllClaims(token);
        List<String> roles = (List<String>) claims.get("roles");

        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))  // 加上 "ROLE_"
                .collect(Collectors.toList());
    }

    public Long extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("userId", Long.class));
    }


}
