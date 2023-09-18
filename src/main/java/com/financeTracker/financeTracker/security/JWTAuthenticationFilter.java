package com.financeTracker.financeTracker.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private  JwtTokenProvider jwtTokenProvider;
    @Autowired
    private  CustomUserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            if (request.getHeader("Authorization") == null){
                filterChain.doFilter(request,response);
                return;
            }

            String jwt = getJwtFromRequest(request);
            String username  =  jwtTokenProvider.getUsernameFromJwtToken(jwt);
            if (null != username){
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtTokenProvider.isTokenValid(jwt,userDetails)){
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            filterChain.doFilter(request,response);
        }catch (Exception ex){
            if (!response.isCommitted()){
                response.sendError(400,String.valueOf(HttpServletResponse.SC_BAD_REQUEST));
            }

            filterChain.doFilter(request,response);
        }
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String tokenPrefix = "Bearer ";
        if(StringUtils.hasText(token)){
            return token.substring(tokenPrefix.length());
        }
        return null;
    }
}
