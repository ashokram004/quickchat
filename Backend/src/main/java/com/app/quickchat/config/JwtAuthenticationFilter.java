package com.app.quickchat.config;

import com.app.quickchat.config.JWTConfig;
import com.app.quickchat.model.User;
import com.app.quickchat.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JWTConfig jwtConfig;
    private final UserService userService;

    public JwtAuthenticationFilter(JWTConfig jwtConfig, UserService userService) {
        this.jwtConfig = jwtConfig;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            try {
                String mobileNo = jwtConfig.extractUsername(token);

                if (mobileNo != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    User user = userService.getUser(mobileNo);

                    if (user != null && jwtConfig.validateToken(token, user.getMobileNo())) {
                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(user, null, null);
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
            } catch (ExpiredJwtException e) {
                logger.warn("JWT token has expired: {}", e);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token has expired");
                return;
            } catch (Exception e) {
                logger.error("Error validating token: {}", e);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid token");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}