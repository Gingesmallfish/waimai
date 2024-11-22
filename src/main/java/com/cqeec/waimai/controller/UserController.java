package com.cqeec.waimai.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.cqeec.waimai.bean.Employee;
import com.cqeec.waimai.exception.CastResultException;
import com.cqeec.waimai.service.EmployeeService;
import com.cqeec.waimai.service.impl.EmployeeServiceImpl;
import com.cqeec.waimai.util.Result;
import com.cqeec.waimai.util.SystemConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
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
        initParams = {@WebInitParam(name = "captchaType",value = "ArithmeticCaptcha")},
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
                captcha = new ArithmeticCaptcha(100,35,2);
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
        //获取用户的登录信息
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String verifyCode = req.getParameter("verify");
        boolean rememberMe = "on".equals(req.getParameter("rememberMe"));
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter out = resp.getWriter();
        //验证登录信息是否完整
        if(StrUtil.hasBlank(username) || StrUtil.hasBlank(password) || StrUtil.hasBlank(verifyCode)){
            out.print(objectMapper.writeValueAsString(new Result(Result.FAIL,"登录信息未填写完整")));
        }else{
            //先判断验证码是否正确
            if(req.getSession().getAttribute("verifyCode").toString().equalsIgnoreCase(verifyCode)){
                //验证码用户名与密码是否正确
                try {
                    Result result = employeeService.login(username,password);
                    if(result.code == Result.SUCCESS){
                        //将用户数据存储在session中以此记录用户的登录状态
                        req.getSession().setAttribute("user",(Employee)result.rows);
                        //利用cookie存储用户名与密码
                        if(rememberMe){
                            Cookie cookie_username = new Cookie("username", username);
                            //利用AES加密算法将密码加密后存储到cookie中
                            Cookie cookie_password = new Cookie("password", SecureUtil.aes(SystemConfig.key.getBytes()).encryptHex(password));
                            cookie_username.setMaxAge(60 * 60 * 24 * 3);//设置有效期为3天
                            cookie_username.setPath("/");//设置有效路径为整个网站
                            cookie_password.setPath("/");
                            cookie_password.setMaxAge(60 * 60 * 24 * 3);
                            resp.addCookie(cookie_username);
                            resp.addCookie(cookie_password);
                        }
                    }
                    out.print(objectMapper.writeValueAsString(result));
                } catch (SQLException | CastResultException e) {
                    handleException(req,resp,e);
                }
            }else{
                out.print(objectMapper.writeValueAsString(new Result(Result.FAIL,"验证码错误")));
            }
        }
    }

    private void logout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //删除session中存储的用户数据
        req.getSession().removeAttribute("user");
        resp.getWriter().print(new ObjectMapper().writeValueAsString(new Result(Result.SUCCESS,"注销成功")));
    }

    private void edit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        String oldpassword = req.getParameter("oldpassword");
        String newpassword = req.getParameter("newpassword");
        String repassword = req.getParameter("repassword");
        if(StrUtil.hasBlank(oldpassword) || StrUtil.hasBlank(newpassword) || StrUtil.hasBlank(repassword)){
            out.print(objectMapper.writeValueAsString(new Result(Result.FAIL,"数据未填写完整")));
        }else{
            //验证两次密码输入是否一致
            if(newpassword.equals(repassword)){
                Employee user = (Employee) req.getSession().getAttribute("user");
                //验证旧密码是否正确
                if(SecureUtil.md5(oldpassword).equals(user.getPassword())){
                    //将新密码更新到user对象中
                    user.setPassword(SecureUtil.md5(newpassword));
                    try {
                        //修改密码
                        employeeService.updatePassword(user);
                        out.print(objectMapper.writeValueAsString(new Result(Result.SUCCESS,"操作成功")));
                    } catch (SQLException e) {
                        handleException(req,resp,e);
                    }
                }else{
                    out.print(objectMapper.writeValueAsString(new Result(Result.FAIL,"旧密码错误")));
                }
            }else{
                out.print(objectMapper.writeValueAsString(new Result(Result.FAIL,"两次密码输入不一致")));
            }
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        captchaType = config.getInitParameter("captchaType");
        employeeService = new EmployeeServiceImpl();
    }
}
