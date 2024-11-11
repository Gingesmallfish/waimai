package com.cqeec.waimai.controller;

import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 *
 **/
@WebServlet(
        value = "/user/*",
        initParams = {@WebInitParam(name = "captchaType",value = "GifCaptcha")},
        loadOnStartup = 1)
public class UserController extends BaseController{
    String captchaType;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String[] params = req.getPathInfo().split("/");
        if (params.length <= 1) {
            this.login(req, resp);
        }else{
            switch (params[1]) {
                case "login":
                    this.login(req, resp);
                    break;
                case "logout":
                    this.logout(req, resp);
                    break;
                case "verify":
                    this.verify(req, resp);
                    break;
                case "edit":
                    this.edit(req,resp);
                    break;
                default:
                    handleException(req, resp, 404, "无法找到请求的资源");
            }
        }
    }

    private void verify(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Captcha captcha = null;
        switch (captchaType){
            case "SpecCaptcha":
                captcha = new SpecCaptcha(100,35,4);
                break;
            case "GifCaptcha":
                captcha = new GifCaptcha(100,35,4);
                break;
            case "ArithmeticCaptcha":
                captcha = new ArithmeticCaptcha(100,35,3);
                break;
        }
        req.getSession().setAttribute("verifyCode",captcha.text());
        resp.setHeader("Pragma", "no-cache");
        resp.setHeader("Cache-Control", "no-cache");
        resp.setDateHeader("Expires", 0);
        resp.setContentType("image/png");
        captcha.out(resp.getOutputStream());
    }

    private void login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    private void logout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    private void edit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        captchaType = config.getInitParameter("captchaType");
    }
}
