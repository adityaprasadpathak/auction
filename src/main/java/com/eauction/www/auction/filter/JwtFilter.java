package com.eauction.www.auction.filter;

import com.eauction.www.auction.security.RequestContext;
import com.eauction.www.auction.service.MyUserDetailsService;
import com.eauction.www.auction.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    MyUserDetailsService myUserDetailsService;

    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    RequestContext requestContext;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
            FilterChain filterChain) throws ServletException, IOException {

        String username = null;
        String jwt = null;
        final String authorizationHeader = httpServletRequest.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            username = jwtUtil.extractUsername(jwt);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);
            if (jwtUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }

            System.out.println(userDetails.getAuthorities().getClass());
            requestContext.setUsername(userDetails.getUsername());
            requestContext.setAdmin(
                    !CollectionUtils.isEmpty(userDetails.getAuthorities()) ? userDetails.getAuthorities().stream()
                            .filter(auth -> auth.toString().equals("ROLE_ADMIN")).findFirst().isPresent() : false);

        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);

    }
}
