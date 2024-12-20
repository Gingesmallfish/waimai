package com.cqeec.waimai.filter;


import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Web过滤器: 建立在客户端与服务器之间，主要的功能是对请求作为预处理操作（可以拦截所有请求）
 * 实现日志记录，权限控制等功能
 * 多个过滤器执行的顺序： 按过滤器的名字排序先后执行
 * 拦截器（SpringMvc）: 针对于特定请求的拦击
 *
 */
@WebFilter(

        urlPatterns = {"/*"}, // 拦截过滤器的规制
        initParams = {@WebInitParam(
                name = "excludeUrl", // 不拦截过滤器的请求
                value = "/user_/login,/error.jsp,/login.jsp,/user_/verify,/autoLogin"
        ), @WebInitParam(
                name = "excludeSuffix", // 排除静态资源文件（css, image, js等等）
                value = ".css,.js,.jpg,.png,.gif,.ico,.html,.htm,.txt,.xml,.json,.md,.svg,.woff,.woff2,.ttf,.eot,.otf"
        )}
)
public class WmFilter implements Filter {
    private String[] excludeUrls;
    private String[] excludeSuffix;
    private static final String LOGIN_PAGE = "/login.jsp";

    // 假设其他必要的方法和构造函数已经实现

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String contextPath = httpRequest.getContextPath();
        if (isAjaxRequest(httpRequest)) {
            httpResponse.setContentType("text/html;charset=UTF-8");
        }

        // 检查排除的URLs
        if (isExcludedUrl(contextPath, excludeUrls, httpRequest)) {
            // 放行（把请求提交）
            chain.doFilter(httpRequest, httpResponse);
            return;
        }
        // 检查排除的后缀
        if (isExcludedSuffix(excludeSuffix, httpRequest)) {
            chain.doFilter(httpRequest, httpResponse);
            return;
        }
        // 检查用户是否已登录
        if (httpRequest.getSession().getAttribute("user") == null) {
            httpResponse.sendRedirect("/autoLogin?pageUrl="+ httpRequest.getRequestURI());
        } else {
            // 判断当前请求用户的权限
            chain.doFilter(httpRequest, httpResponse);
        }
    }

    private boolean isExcludedUrl(String contextPath, String[] excludeUrls, HttpServletRequest request) {
        if (excludeUrls == null || excludeUrls.length == 0) return false;
        if (contextPath.equals("/")) contextPath = "";
        for (String suffix : excludeUrls) {
            String fullSuffix = contextPath + suffix;
            if (fullSuffix.equals(request.getRequestURI())) {
                return true;
            }
        }
        return false;
    }

    private boolean isExcludedSuffix(String[] excludeSuffix, HttpServletRequest request) {
        if (excludeSuffix == null || excludeSuffix.length == 0) return false;

        // 优化：使用HashSet提高查找效率
        Set<String> suffixSet = new HashSet<>(Arrays.asList(excludeSuffix));
        String requestURI = request.getRequestURI();

        for (String suffix : suffixSet) {
            if (requestURI.endsWith(suffix)) {
                return true;
            }
        }
        return false;
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        this.excludeUrls = filterConfig.getInitParameter("excludeUrl").split(",");
        this.excludeSuffix = filterConfig.getInitParameter("excludeSuffix").split(",");
    }


    public void destroy() {
        Filter.super.destroy();
    }

    public boolean isAjaxRequest(HttpServletRequest req) {
        String requestType = req.getHeader("X-Requested-With");
        if (requestType != null && requestType.equals("XMLHttpRequest")) {
            return true;
        } else {
            return false;
        }
    }
}
