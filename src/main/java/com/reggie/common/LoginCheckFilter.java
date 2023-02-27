package com.reggie.common;


import com.alibaba.fastjson.JSON;
import com.reggie.constants.EmployeeConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestURI = request.getRequestURI();

        String[] safeUris = {
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**"
        };

        /*
           这里前端的静态页面本身不会被拦截，但是页面打开时会发送一些请求,这些请求导致产生静态页面被拦截的效果
         */
        boolean needCheck = needCheckLoginStatus(safeUris, requestURI);
        // 不需要登录
        if (!needCheck) {
            log.info("本次请求{}不需要处理", requestURI);
            filterChain.doFilter(request, response);
            return;
        }
        // 用户已登录
        if (request.getSession().getAttribute(EmployeeConstants.SESSION_EMPLOYEE_ID_KEY) != null) {
            log.info("用户已登录，用户id为: {}", request.getSession().getAttribute("employee"));
            filterChain.doFilter(request, response);
            return;
        }

        // 需要登录但用户未登录
        log.info("本次请求{}被拦截", requestURI);
        response.getWriter().write(JSON.toJSONString(JsonResponse.error("NOTLOGIN")));
    }

    private boolean needCheckLoginStatus(String[] uris, String requestURI) {
        for (String u : uris) {
            boolean match = PATH_MATCHER.match(u, requestURI);
            if (match) {
                return false;
            }
        }
        return true;
    }
}
