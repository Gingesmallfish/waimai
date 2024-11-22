package com.cqeec.waimai.controller;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;

public class BaseController extends HttpServlet {
    public static String contextPath;  //保存上下文路径，在servletcontext监听器中设置

    protected void handleException(HttpServletRequest req, HttpServletResponse resp, Exception e) {
        handleException(req, resp, 100 ,e.getMessage());
    }

    protected void handleException(HttpServletRequest req, HttpServletResponse resp, int code,String message){
        try {
            resp.sendRedirect(String.format("%s/error.jsp?code=%d&message=%s",
                    contextPath, code, URLEncoder.encode(message,"utf-8")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            doPost(req, resp);
        } catch (Exception e) {
            handleException(req, resp, e);
        }
    }
}
