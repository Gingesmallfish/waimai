package com.cqeec.waimai.controller;

import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.cqeec.waimai.bean.Employee;
import com.cqeec.waimai.exception.CastResultException;
import com.cqeec.waimai.service.EmployeeService;
import com.cqeec.waimai.service.impl.EmployeeServiceImpl;
import com.cqeec.waimai.util.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@WebServlet(value = "/employee_/*")
public class EmployeeController extends BaseController {

    public EmployeeService employeeService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        employeeService = new EmployeeServiceImpl();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String[] params = req.getPathInfo().split("/");
        if (params.length <= 1) {
            this.list(req, resp);
        } else {
            switch (params[1]) {
                case "list":
                    this.list(req, resp);
                    break;
                case "disable":
                    this.disable(req, resp);
                    break;
                case "enable":
                    this.enable(req, resp);
                    break;
                case "save":
                    this.save(req, resp);
                    break;
                default:
                    handleException(req, resp, 404, "无法找到请求的资源");
            }
        }
    }


    /**
     * 处理员工信息保存请求的方法
     * 该方法负责解析HTTP请求中的员工信息，验证信息的完整性与合法性，并调用服务层方法保存员工信息
     *
     * @param req 用于获取请求参数和会话信息的HttpServletRequest对象
     * @param resp 用于向客户端输出响应内容的HttpServletResponse对象
     * @throws ServletException 如果Servlet操作出错
     * @throws IOException 如果输入输出操作出错
     */
    private void save(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取用于向客户端输出响应内容的PrintWriter对象
        PrintWriter out = resp.getWriter();
        // 创建ObjectMapper对象，用于将Java对象转换为JSON格式以便在网络传输中使用
        ObjectMapper objectMapper = new ObjectMapper();

        // 获取请求参数，这些参数将用于新增或修改员工信息
        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String sex = req.getParameter("sex");
        String username = req.getParameter("username");
        String idNumber = req.getParameter("id_number");
        String phone = req.getParameter("phone");

        // 检查员工信息中的必填字段是否有空白值（为空或仅包含空格）
        // 如果有任何一个必填字段为空，将返回错误信息给客户端
        if (StrUtil.hasBlank(name) || StrUtil.hasBlank(sex) || StrUtil.hasBlank(username) || StrUtil.hasBlank(idNumber) ||
                StrUtil.hasBlank(phone)) {
            // 构造一个表示失败的Result对象，消息为“员工数据未填写完整”
            // 并将其转换为JSON格式，通过out输出给客户端
            out.print(objectMapper.writeValueAsString(new Result(Result.FAIL, "员工数据未填写完整")));
        } else {
            // 验证员工的身份证号和手机号格式是否正确
            if (IdcardUtil.isValidCard(idNumber) && PhoneUtil.isPhone(phone)) {
                try {
                    // 创建一个Employee对象，用于存储即将新增或修改的员工信息
                    Employee employee = new Employee();

                    // 判断是否为新增操作（如果id为空字符串或空白字符串，则视为新增）
                    if (StrUtil.hasBlank(id)) {
                        // 设置员工的id为0，可能在数据库中0表示新增记录的默认标识
                        employee.setId(0);

                        // 将员工的初始状态设置为1，这里的1可能代表某种特定的初始状态，具体含义由业务逻辑决定
                        employee.setStatus(1);

                        // 以身份证号后六位作为初始密码，先通过MD5加密处理
                        employee.setPassword(SecureUtil.md5(idNumber.substring(idNumber.length() - 6)));

                        // 设置员工的其他信息，如姓名、性别、用户名、身份证号、手机号等
                        employee.setName(name);
                        employee.setSex(sex);
                        employee.setUsername(username);
                        employee.setId_number(idNumber);
                        employee.setPhone(phone);

                        // 调用employeeService的save方法来保存新增的员工信息
                        // 这里传入employee对象和其id（此处为0），由具体的实现来处理保存到数据库等操作
                        boolean r = employeeService.save(employee, employee.getId());
                        if (r) {
                            // 如果保存成功，构造一个表示成功的Result对象，消息为“添加成功”
                            // 并将其转换为JSON格式，通过out输出给客户端
                            out.print(objectMapper.writeValueAsString(new Result(Result.SUCCESS, "添加成功")));
                        } else {
                            // 如果保存失败，构造一个表示失败的Result对象，消息为“添加失败”
                            // 并将其转换为JSON格式，通过out输出给客户端
                            out.print(objectMapper.writeValueAsString(new Result(Result.FAIL, "添加失败")));
                        }
                    } else {
                        // 如果不是新增操作，即为修改操作
                        // 根据传入的员工id查询当前员工信息，通过调用employeeService的getEmployeeById方法
                        employee = employeeService.getEmployeeById(Long.parseLong(id));

                        // 设置员工的新信息，如姓名、用户名、手机号、身份证号、性别等
                        employee.setName(name);
                        employee.setUsername(username);
                        employee.setPhone(phone);
                        employee.setId_number(idNumber);
                        employee.setSex(sex);

                        // 从会话（session）中获取当前登录员工的id
                        long userId = ((Employee) req.getSession().getAttribute("user")).getId();

                        // 调用employeeService的save方法来保存修改后的员工信息
                        // 这里传入修改后的employee对象和当前登录员工的id，由具体的实现来处理保存到数据库等操作
                        boolean r = employeeService.save(employee, userId);
                        if (r) {
                            // 如果保存成功，构造一个表示成功的Result对象，消息为“操作成功”
                            // 并将其转换为JSON格式，通过out输出给客户端
                            out.print(objectMapper.writeValueAsString(new Result(Result.SUCCESS, "操作成功")));
                        } else {
                            // 如果保存失败，构造一个表示失败的Result对象，消息为“登陆账号重复”
                            // 并将其转换为JSON格式，通过out输出给客户端
                            out.print(objectMapper.writeValueAsString(new Result(Result.FAIL, "登陆账号重复")));
                        }
                    }
                } catch (SQLException | CastResultException e) {
                    // 如果在保存员工信息过程中出现SQLException或CastResultException异常，
                    // 调用handleException方法进行异常处理，具体的异常处理逻辑可能在handleException方法中定义
                    handleException(req, resp, e);
                    // 重新抛出RuntimeException，以便在更上层的调用栈中能捕获到这个异常并进行处理
                    throw new RuntimeException(e);
                }
            } else {
                // 如果员工的身份证号或手机号格式不正确，构造一个表示失败的Result对象，消息为“员工身份证号或手机号错误”
                // 并将其转换为JSON格式，通过out输出给客户端
                out.print(objectMapper.writeValueAsString(new Result(Result.FAIL, "员工身份证号或手机号错误")));
            }
        }
    }


    /**
     * 禁用员工账户
     *
     * 该方法从HTTP请求中提取员工ID参数，并尝试禁用该员工账户如果员工ID为空或无效，
     * 则向响应中写入失败结果；否则，尝试禁用员工账户，并根据操作结果向响应中写入成功或失败信息
     *
     * @param req HTTP请求对象，用于获取请求参数和会话信息
     * @param resp HTTP响应对象，用于向客户端写入响应数据
     * @throws ServletException 如果Servlet操作失败
     * @throws IOException 如果I/O操作失败
     */
    private void disable(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取请求参数中的员工ID
        String id = req.getParameter("id");
        // 获取响应输出流，用于向客户端写入响应数据
        PrintWriter out = resp.getWriter();
        // 创建ObjectMapper实例，用于JSON序列化
        ObjectMapper objectMapper = new ObjectMapper();

        // 检查员工ID是否为空或仅包含空白字符
        if (StrUtil.hasBlank(id)) {
            // 如果员工ID无效，向响应中写入失败结果
            out.print(objectMapper.writeValueAsString(new Result(Result.FAIL,"员工id不能为空")));
        } else {
            // 尝试禁用员工账户
            try {
                // 调用employeeService的disable方法禁用员工账户，并传递当前用户ID作为操作者标识
                boolean r = employeeService.disable(Long.parseLong(id),((Employee)req.getSession().getAttribute("user")).getId());
                if (r) {
                    // 如果操作成功，向响应中写入成功结果
                    out.print(objectMapper.writeValueAsString(new Result(Result.SUCCESS, "操作成功")));
                } else {
                    // 如果操作失败，向响应中写入失败结果
                    out.print(objectMapper.writeValueAsString(new Result(Result.FAIL, "操作失败")));
                }
            } catch (SQLException | CastResultException e) {
                // 处理可能的异常，包括数据库操作异常和类型转换异常
                handleException(req,resp,e);
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 启用员工账户
     *
     * @param req 用于获取请求参数和会话信息
     * @param resp 用于向客户端发送响应数据
     * @throws ServletException 如果Servlet操作失败
     * @throws IOException 如果输入/输出操作失败
     */
    private void enable(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取请求参数中的员工ID
        String id = req.getParameter("id");
        // 获取响应输出流，用于向客户端发送数据
        PrintWriter out = resp.getWriter();
        // 创建ObjectMapper实例，用于JSON序列化
        ObjectMapper objectMapper = new ObjectMapper();

        // 检查员工ID是否为空或全空白
        if (StrUtil.hasBlank(id)) {
            // 如果员工ID为空或全空白，则返回失败结果
            out.print(objectMapper.writeValueAsString(new Result(Result.FAIL,"员工id不能为空")));
        } else {
            try {
                // 调用业务服务启用员工账户，并传递当前操作用户ID
                boolean r = employeeService.enable(Long.parseLong(id), ((Employee)req.getSession().getAttribute("user")).getId());
                if (r) {
                    // 如果操作成功，则返回成功结果
                    out.print(objectMapper.writeValueAsString(new Result(Result.SUCCESS, "操作成功")));
                } else {
                    // 如果操作失败，则返回失败结果
                    out.print(objectMapper.writeValueAsString(new Result(Result.SUCCESS, "操作失败")));
                }
            } catch (SQLException | CastResultException e) {
                // 处理SQL异常或类型转换异常
                handleException(req,resp,e);
                throw new RuntimeException(e);
            }
        }
    }



    /**
     * 处理员工列表请求的方法
     *
     * @param req 用于获取请求参数和设置响应内容的HttpServletRequest对象
     * @param resp 用于向客户端发送响应的HttpServletResponse对象
     * @throws ServletException 如果Servlet遇到异常
     * @throws IOException 如果发生输入或输出异常
     */
    private void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 创建查询条件Map
        Map<String, Object> where = new HashMap<>();
        // 获取请求参数
        String name = req.getParameter("employeeName");
        String status = req.getParameter("employeeStatus");
        // 判断员工姓名不为空，则添加到查询条件中
        if (StrUtil.hasBlank(name)) {
            where.put("name", name);
        }
        // 如果员工状态不为空不等于 -1，则添加假查询条件中
        if ((!StrUtil.hasBlank(status)) && (!status.equals("-1"))) {
            where.put("status", status);
        }
        // 设置分页参数
        int currentPage = 1;  // 当前也页页码
        int recordsPerPge = 5; // 每页显示记录数

        // 如果请求中包含page参数，则更新当前页码
        if (!StrUtil.hasBlank(req.getParameter("page"))) {
            try {
                currentPage = Integer.parseInt(req.getParameter("page"));
            } catch (Exception e) {
                // 忽略异常，使用默认页码
            }
        }

        // 如果请求中包含"rows"参数，则更新每页显示的记录数
        if (!StrUtil.hasBlank(req.getParameter("rows"))) {
            try {
                recordsPerPge = Integer.parseInt(req.getParameter("rows"));
            } catch (Exception e) {
                // 忽略异常，使用默认记录数
            }
        }
        try {
            // 获取符合条件的总记录数
            int total = employeeService.getTotal(where);
            // 获取当前页的员工列表
            ArrayList<Employee> employees = employeeService.list(where, currentPage, recordsPerPge);
            // 1）获取响应的PrintWriter对象
            PrintWriter out = resp.getWriter();
            // 2）创建ObjectMapper对象用于将结果转为JSON格式
            ObjectMapper objectMapper = new ObjectMapper();

            // 输出结果
            out.print(objectMapper.writeValueAsString(
                    new Result(Result.SUCCESS, "操作成功", total, employees)
            ));
        } catch (SQLException | CastResultException e) {
            // 处理异常
            handleException(req, resp, e);
        }
    }
}

