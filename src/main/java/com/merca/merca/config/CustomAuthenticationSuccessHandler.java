package com.merca.merca.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Set;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                      Authentication authentication) throws IOException, ServletException {
        
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        
        String redirectURL = request.getContextPath();
        
        if (roles.contains("ROLE_ADMIN")) {
            redirectURL += "/admin/dashboard";
        } else if (roles.contains("ROLE_COMERCIAL")) {
            redirectURL += "/comercial/dashboard";
        } else if (roles.contains("ROLE_TIENDA")) {
            redirectURL += "/tienda/dashboard";
        } else {
            redirectURL += "/dashboard";
        }
        
        response.sendRedirect(redirectURL);
    }
}
