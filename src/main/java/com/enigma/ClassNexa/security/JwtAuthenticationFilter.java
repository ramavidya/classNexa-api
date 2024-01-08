package com.enigma.ClassNexa.security;

import com.enigma.ClassNexa.model.JwtClaim;
import com.enigma.ClassNexa.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserService userService;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = parseJTW(request);
            if (token != null && jwtUtil.VeryFyJwtToken(token)){
                JwtClaim userInfo = jwtUtil.getUserInfoByToken(token);
                UserDetails userDetails = userService.loadUserById(userInfo.getUserId());
                UsernamePasswordAuthenticationToken authenticatin = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                authenticatin.setDetails(new WebAuthenticationDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticatin);
            }
        }catch (Exception e){
            log.error("cannot set user authentication: {} ", e.getMessage());
        }
        filterChain.doFilter(request,response);
    }

    private String parseJTW(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")){
            return token.substring(7);
        }
        return null;
    }
}
