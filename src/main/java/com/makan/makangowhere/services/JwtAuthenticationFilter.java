package com.makan.makangowhere.services;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final AuthService authService;

    public JwtAuthenticationFilter(AuthService authService) {
        this.authService = authService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String email = authService.getEmailFromToken(jwt);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, null,
                    null);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    // @Override
    // protected void doFilterInternal(HttpServletRequest request,
    // HttpServletResponse response, FilterChain filterChain) {
    // try {
    // String authHeader = request.getHeader("Authorization");
    // if (authHeader != null && authHeader.startsWith("Bearer ")) {
    // String jwt = authHeader.substring(7);
    // String email = authService.getEmailFromToken(jwt);
    // UsernamePasswordAuthenticationToken authentication = new
    // UsernamePasswordAuthenticationToken(email,
    // null, null);
    // SecurityContextHolder.getContext().setAuthentication(authentication);
    // }
    // filterChain.doFilter(request, response);
    // } catch (Exception ex) {
    // // Handle the exception
    // ex.printStackTrace();
    // throw (ex);
    // }
    // }

}
