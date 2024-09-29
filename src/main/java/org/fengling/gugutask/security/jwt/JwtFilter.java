package org.fengling.gugutask.security.jwt;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter implements Filter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 可选：初始化时的操作
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 提取 Authorization header
        String authorizationHeader = httpRequest.getHeader("Authorization");

        String token = null;
        String username = null;

        // 检查 Authorization 是否包含 JWT token 且以 "Bearer " 开头
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // 提取 JWT token

            try {
                username = jwtUtil.extractUsername(token);  // 从 token 中提取用户名
            } catch (Exception e) {
                // 处理 token 无效的情况
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpResponse.getWriter().write("Invalid or expired token");
                return;
            }
        }

        // 如果 token 存在，且未进行过身份认证，则进行 token 验证
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtUtil.validateToken(token, username)) {  // 验证 token 是否有效
                Long userId = jwtUtil.extractUserId(token);  // 从 token 中提取userId
                List<GrantedAuthority> authorities = jwtUtil.getAuthorities(token);

                // 创建 UsernamePasswordAuthenticationToken，包含 userId, username 和权限
                UsernamePasswordAuthenticationToken authenticationToken = new JwtToken(userId, jwtUtil.getAuthorities(token), token);
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));

                // 设置认证信息到 Spring Security 上下文
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                // token 验证失败，返回 401 状态码
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpResponse.getWriter().write("Token validation failed");
                return;
            }
        }

        // 放行请求到下一个过滤器或处理器
        chain.doFilter(request, response);
        System.out.println("Authentication: " + SecurityContextHolder.getContext().getAuthentication());
    }

    @Override
    public void destroy() {
        // 可选：销毁时的操作
    }
}
