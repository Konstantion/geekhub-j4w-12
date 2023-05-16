package com.konstantion.security.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import static com.konstantion.utils.EntityNameConstants.JWT_TOKEN;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String newToken = request.getHeader("Authorization");
        String oldToken = (String) request.getSession().getAttribute(JWT_TOKEN);
        if (oldToken == null || !oldToken.equals(newToken)) {
            request.getSession().invalidate();
        }
        request.getSession().setAttribute(JWT_TOKEN, newToken);
        return true;
    }
}
