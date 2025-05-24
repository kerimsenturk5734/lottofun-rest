package com.lottofun.lottofunrest.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private static List<String> skipFilterUrls = Arrays.asList("/api/v1/auth/login", "/api/v1/auth/register");

    public JwtFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // Get 'Authorization' header from request context
        final String authHeader = request.getHeader("Authorization");

        String username = null, token = null;

        // Validate header not null and starts with 'Bearer' keyword
        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            // get token from header
            token = authHeader.substring(7);

            // extract username from token
            username = jwtUtil.extractUsername(token);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // load user details
            var userDetails = userDetailsService.loadUserByUsername(username);

            // check token is valid
            if (jwtUtil.validateToken(token)) {
                // create and set authentication for this user
                var authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // set authentication for this http context
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return skipFilterUrls.stream().anyMatch(url -> new AntPathRequestMatcher(url).matches(request));
    }
}
