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

    /**
     * 用户登录
     *
     * @param req  请求对象
     * @param resp 响应对象
     * @throws ServletException 异常
     * @throws IOException      异常
     */
    private void login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 获取用户的登录信息
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String verifyCode = req.getParameter("verify");

        // 判断用户是否勾选了“记住我”选项，若请求参数中"rememberMe"的值为"on"，则表示勾选，设置rememberMe为true，否则为false
        boolean rememberMe = "on".equals(req.getParameter("rememberMe"));

        // 创建ObjectMapper对象，用于后续将Java对象转换为JSON字符串
        ObjectMapper objectMapper = new ObjectMapper();
        // 获取用于向客户端输出响应内容的PrintWriter对象
        PrintWriter out = resp.getWriter();

        // 验证登录信息是否完整，即用户名、密码和验证码都不能为空
        if (StrUtil.hasBlank(username) || StrUtil.hasBlank(password) || StrUtil.hasBlank(verifyCode)) {
            // 如果有任何一个登录信息为空，构造一个表示失败的Result对象，消息为“登录信息未填写完整”
            // 并将其转换为JSON字符串后输出到客户端
            out.print(objectMapper.writeValueAsString(new Result(Result.FAIL, "登录信息未填写完整")));
        } else {
            // 先判断验证码是否正确
            // 从会话（session）中获取之前存储的验证码，并与用户输入的验证码进行不区分大小写的比较
            if (req.getSession().getAttribute("verifyCode").toString().equalsIgnoreCase(verifyCode)) {
                // 验证码正确，接着验证用户名与密码是否正确
                try {
                    // 调用employeeService的login方法进行用户名和密码的验证，该方法返回一个Result对象，包含验证结果等信息
                    Result result = employeeService.login(username, password);
                    // 如果登录验证成功，将用户数据存储在session中，以此记录用户的登录状态
                    if (result.code == Result.SUCCESS) {
                        // 这里假设result.rows存储了用户相关数据，且类型为Employee，将其设置为会话中的"user"属性值
                        req.getSession().setAttribute("user", (Employee) result.rows);

                        // 如果用户勾选了“记住我”选项
                        if (rememberMe) {
                            // 创建一个名为"username"的Cookie，用于存储用户名，并设置其值为获取到的用户名
                            Cookie cookie_username = new Cookie("username", username);
                            // 利用AES加密算法将密码加密，先将系统配置中的密钥转换为字节数组，再对密码进行加密并转换为十六进制字符串形式
                            // 然后创建一个名为"password"的Cookie，用于存储加密后的密码
                            Cookie cookie_password = new Cookie("password", SecureUtil.aes(SystemConfig.key.getBytes()).encryptHex(password));

                            // 设置"username" Cookie的有效期为3天，单位是秒（60秒 * 60分钟 * 24小时 * 3天）
                            cookie_username.setMaxAge(60 * 60 * 24 * 3);
                            // 设置"username" Cookie的有效路径为整个网站，即"/"，表示在该网站的所有页面都可以访问到该Cookie
                            cookie_username.setPath("/");
                            // 同样设置"password" Cookie的有效路径为整个网站
                            cookie_password.setPath("/");
                            // 设置"password" Cookie的有效期为3天
                            cookie_password.setMaxAge(60 * 60 * 24 * 3);

                            // 将创建好的"username"和"password" Cookie添加到响应中，发送给客户端，以便客户端保存这些信息
                            resp.addCookie(cookie_username);
                            resp.addCookie(cookie_password);
                        }
                    }
                    // 将验证结果（Result对象）转换为JSON字符串后输出到客户端
                    out.print(objectMapper.writeValueAsString(result));

                    // 如果在验证用户名和密码的过程中出现SQLException或CastResultException异常，
                } catch (SQLException | CastResultException e) {

                    // 调用handleException方法进行异常处理，该方法具体实现可能在其他地方定义，用于统一处理这类异常情况
                    handleException(req, resp, e);
                }
            } else {
                // 如果验证码不正确，构造一个表示失败的Result对象，消息为“验证码错误”
                // 并将其转换为JSON字符串后输出到客户端
                out.print(objectMapper.writeValueAsString(new Result(Result.FAIL, "验证码错误")));
            }
        }
    }

    private void logout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //删除session中存储的用户数据
        req.getSession().removeAttribute("user");
        resp.getWriter().print(new ObjectMapper().writeValueAsString(new Result(Result.SUCCESS,"注销成功")));
    }

    private void edit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取用于向客户端输出响应内容的PrintWriter对象
        PrintWriter out = resp.getWriter();
// 创建ObjectMapper对象，用于将Java对象转换为JSON格式以便在网络传输中使用
        ObjectMapper objectMapper = new ObjectMapper();

// 获取用户输入的旧密码、新密码和再次输入的新密码（用于确认）
        String oldpassword = req.getParameter("oldpassword");
        String newpassword = req.getParameter("newpassword");
        String repassword = req.getParameter("repassword");

// 检查输入的旧密码、新密码和再次输入的新密码是否有空白值（为空或仅包含空格）
        if (StrUtil.hasBlank(oldpassword) || StrUtil.hasBlank(newpassword) || StrUtil.hasBlank(repassword)) {
            // 如果有任何一个密码字段为空或仅包含空格，构造一个表示失败的Result对象，消息为“数据未填写完整”
            // 并将其转换为JSON格式，通过out输出给客户端
            out.print(objectMapper.writeValueAsString(new Result(Result.FAIL, "数据未填写完整")));
        } else {
            // 验证两次输入的新密码是否一致
            if (newpassword.equals(repassword)) {
                // 从会话（session）中获取当前登录的员工对象
                Employee user = (Employee) req.getSession().getAttribute("user");

                // 验证用户输入的旧密码是否正确，通过将输入的旧密码进行MD5加密后与存储在用户对象中的密码进行比较
                if (SecureUtil.md5(oldpassword).equals(user.getPassword())) {
                    // 如果旧密码正确，将新密码进行MD5加密后更新到用户对象的密码属性中
                    user.setPassword(SecureUtil.md5(newpassword));

                    try {
                        // 调用employeeService的updatePassword方法来更新数据库中的用户密码信息
                        employeeService.updatePassword(user);
                        // 如果更新成功，构造一个表示成功的Result对象，消息为“操作成功” 并将其转换为JSON格式，通过out输出给客户端
                        out.print(objectMapper.writeValueAsString(new Result(Result.SUCCESS, "操作成功")));
                    } catch (SQLException e) {
                        // 如果在更新密码过程中出现SQLException异常，调用handleException方法进行异常处理
                        handleException(req, resp, e);
                    }
                } else {
                    // 并将其转换为JSON格式，通过out输出给客户端，如果旧密码错误，构造一个表示失败的Result对象，消息为“旧密码错误”
                    out.print(objectMapper.writeValueAsString(new Result(Result.FAIL, "旧密码错误")));
                }
            } else {
                // 如果两次输入的新密码不一致，构造一个表示失败的Result对象，消息为“两次密码输入不一致”
                // 并将其转换为JSON格式，通过out输出给客户端
                out.print(objectMapper.writeValueAsString(new Result(Result.FAIL, "两次密码输入不一致")));
            }
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        captchaType = config.getInitParameter("captchaType");
        employeeService = new EmployeeServiceImpl();
    }
}
