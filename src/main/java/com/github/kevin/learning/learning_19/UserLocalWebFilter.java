package com.github.kevin.learning.learning_19;


import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;

@Component
public class UserLocalWebFilter implements Filter {

    private static final String HEADER_USER_ID = "X-User-Id";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String userId = httpRequest.getHeader(HEADER_USER_ID);

        if (userId != null && !userId.isEmpty()) {
            // 模拟从权限系统中获取用户信息
            SessionUserLocal.setUser(User.builder().userId(userId).name("test").permissions(Arrays.asList("can_do1", "can_do2")).roles(Arrays.asList("common", "admin")).build());
        }

        try {
            chain.doFilter(request, response);
        } finally {
            SessionUserLocal.clear();
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization logic if needed
    }

    @Override
    public void destroy() {
        // Cleanup logic if needed
    }
}
