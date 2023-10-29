package com.muxan.flightschedulingsystem.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/home","/adminPage"})
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();

        String userType = (String) session.getAttribute("userType");
        String requestedUri = request.getRequestURI();

        if (userType == null) {
            response.sendRedirect("/");
            return;
        }


        if ("/adminPage".equals(requestedUri) && !"admin".equals(userType)) {
            response.sendRedirect("//");
            return;
        }

        filterChain.doFilter(request, response);
    }
}

