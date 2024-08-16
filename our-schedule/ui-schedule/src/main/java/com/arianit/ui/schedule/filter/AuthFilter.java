package com.arianit.ui.schedule.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(filterName = "AuthFilter", urlPatterns = {"*.xhtml"})
public class AuthFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // check whether session variable is set
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        HttpSession ses = req.getSession(false);
        //  allow user to proceed if url is login.xhtml or user logged in or user is accessing any page in //public folder
        String reqURI = req.getRequestURI();
        if (reqURI.contains("/login.xhtml") || (ses != null && ses.getAttribute("username") != null)
                || reqURI.contains("/public/") || reqURI.contains("javax.faces.resource"))
            filterChain.doFilter(servletRequest, servletResponse);
        else   // user didn't log in but asking for a page that is not allowed so take user to login page
            res.sendRedirect(req.getContextPath() + "/login.xhtml");  // Anonymous user. Redirect to login page
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
