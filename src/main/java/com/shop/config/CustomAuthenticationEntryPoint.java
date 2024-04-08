package com.shop.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.io.IOException;

//implements AuthenticationEntryPoint
public class CustomAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

    public CustomAuthenticationEntryPoint(String loginUrl) {super(loginUrl);}

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        String ajaxHeader = request.getHeader("X-Requested-With");
        boolean isAjax = (ajaxHeader==null) ? false : ajaxHeader.equals("XMLHttpRequest");
        if(isAjax){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Ajax Request Denied");
        } else {
            super.commence(request, response, authException);
        }

        if("XMLHtpRequest".equals(request.getHeader("x-requested-with"))) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        } else {
            response.sendRedirect("/members/login");
        }
    }
}
