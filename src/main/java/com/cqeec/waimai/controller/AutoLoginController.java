package com.cqeec.waimai.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.cqeec.waimai.bean.Employee;
import com.cqeec.waimai.exception.CastResultException;
import com.cqeec.waimai.service.EmployeeService;
import com.cqeec.waimai.service.impl.EmployeeServiceImpl;
import com.cqeec.waimai.util.Result;
import com.cqeec.waimai.util.SystemConfig;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

/**
 *  自动登录
 **/
@WebServlet(value = "/autoLogin")
public class AutoLoginController extends BaseController {
    EmployeeService employeeService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        employeeService = new EmployeeServiceImpl();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = null;
        String password = null;
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        //获取所有的cookie
        Cookie[] cookies = req.getCookies();
        if(cookies == null){
            out.print("<script>alert('用户未登录或登录状态已失效');window.location.href='login.jsp';</script>");
        }else{
            //遍历cookie找出存储的用户数据（用户名与密码）
            for(Cookie cookie : cookies){
                if("username".equals(cookie.getName())){
                    username = cookie.getValue();
                }
                if("password".equals(cookie.getName())){
                    //利用AES解密密码数据
                    password = SecureUtil.aes(SystemConfig.key.getBytes()).decryptStr(cookie.getValue());
                }
            }
            if(!(StrUtil.hasBlank(username) || StrUtil.hasBlank(password))){
                try {
                    Result result = employeeService.login(username,password);
                    if(result.code == Result.SUCCESS){
                        req.getSession().setAttribute("user",(Employee)result.rows);
                        resp.sendRedirect(req.getParameter("page"));
                    }else{
                        //删除cookie数据
                        for (Cookie cookie : req.getCookies()) {
                            if (cookie.getName().equals("username") || cookie.getName().equals("password")) {
                                cookie.setMaxAge(0);
                                resp.addCookie(cookie);
                            }
                        }
                        out.print("<script>alert('"+result.message+"');window.location.href='login.jsp';</script>");
                    }
                } catch (SQLException | CastResultException e) {
                    handleException(req,resp,e);
                }
            }else{
                out.print("<script>alert('用户未登录或登录状态已失效');window.location.href='login.jsp';</script>");
            }
        }
    }
}
