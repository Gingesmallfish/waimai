package com.cqeec.waimai.controller;

import com.cqeec.waimai.bean.Employee;
import com.cqeec.waimai.exception.CastResultException;
import com.cqeec.waimai.service.EmployeeService;
import com.cqeec.waimai.service.impl.EmployeeServiceImpl;
import com.cqeec.waimai.util.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.io.PrintWriter;
import java.sql.SQLException;

/**
 *
 **/
@WebServlet(
        value = "/user_/*",
        initParams = {@WebInitParam(name = "captchaType",value = "GifCaptcha")},
        loadOnStartup = 1)
public class UserController extends BaseController{
    String captchaType;
    EmployeeService employeeService;

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
        // 获取用户登录的信息
        String username = req.getParameter("username") == null ? "" : req.getParameter("username");
        String password = req.getParameter("password") == null ? "" : req.getParameter("password");
        String verifyCode = req.getParameter("verify") == null ? "" : req.getParameter("verify");
        // 验证登录信息是否完整
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter out = resp.getWriter();
        if (username.equals("") || password.equals("") || verifyCode.equals("")) {
            out.print(objectMapper.writeValueAsString(new Result(Result.FAIL,"登录信息为填写完整")));
        } else {
            // 先判断验证码是否正确
            if (req.getSession().getAttribute("verifyCode").toString().equalsIgnoreCase(verifyCode)) {
                // 验证码用户名于密码是否正确
                try {
                    Result result = employeeService.login(username, password);
                    if (result.code == Result.SUCCESS) {
                        // 将用户数据存在session中 以此记录用户中登录状态
                        req.getSession().setAttribute("user",(Employee)result.rows);
                    }
                    out.print(objectMapper.writeValueAsString(result));
                } catch (SQLException e) {
                   handleException(req,resp,e);
                } catch (CastResultException e) {
                    handleException(req,resp,e);
                }
            } else {
                out.print(objectMapper.writeValueAsString(new Result(Result.FAIL,"验证码错误")));
            }
        }
    }

    private void logout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    private void edit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        captchaType = config.getInitParameter("captchaType");
        employeeService = new EmployeeServiceImpl();
    }
}
