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

    EmployeeService employeeService;

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
     * 这个方法是处理新增于修改操作
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void save(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String sex = req.getParameter("sex");
        String username = req.getParameter("username");
        String idNumber = req.getParameter("id_number");
        String phone = req.getParameter("phone");

        if (StrUtil.hasBlank(name) || StrUtil.hasBlank(sex) || StrUtil.hasBlank(username) || StrUtil.hasBlank(idNumber) ||
                StrUtil.hasBlank(phone)) {
            out.print(objectMapper.writeValueAsString(new Result(Result.FAIL, "员工数据未填写完整")));
        } else {
            if (IdcardUtil.isValidCard(idNumber) && PhoneUtil.isPhone(phone)) {
                try {
                    Employee employee = new Employee();
                    if (StrUtil.hasBlank(id)) {//新增
                        employee.setId(0);
                        //将员工密码和状态存储到employee对象中
                        employee.setStatus(1);
                        //身份证后六位作为初始密码
                        employee.setPassword(SecureUtil.md5(idNumber.substring(idNumber.length() - 6)));
                        //添加数据
                        employee.setName(name);
                        employee.setSex(sex);
                        employee.setUsername(username);
                        employee.setId_number(idNumber);
                        employee.setPhone(phone);
                        boolean r = employeeService.save(employee, employee.getId());
                        if (r) {
                            out.print(objectMapper.writeValueAsString(new Result(Result.SUCCESS, "添加成功")));
                        } else {
                            out.print(objectMapper.writeValueAsString(new Result(Result.FAIL, "添加失败")));
                        }
                    } else {//修改
                        //按照员工id查询当前员工信息
                        employee = employeeService.getEmployeeById(Long.parseLong(id));
                        employee.setName(name);
                        employee.setUsername(username);
                        employee.setPhone(phone);
                        employee.setId_number(idNumber);
                        employee.setSex(sex);
                        //从session中获取当前登录员工
                        long userId = ((Employee) req.getSession().getAttribute("user")).getId();
                        boolean r = employeeService.save(employee, userId);
                        if (r) {
                            out.print(objectMapper.writeValueAsString(new Result(Result.SUCCESS, "操作成功")));
                        } else {
                            out.print(objectMapper.writeValueAsString(new Result(Result.FAIL, "登陆账号重复")));
                        }
                    }
                } catch (SQLException | CastResultException e) {
                    handleException(req, resp, e);
                    throw new RuntimeException(e);
                }
            } else {
                out.print(objectMapper.writeValueAsString(new Result(Result.FAIL, "员工身份证号或手机号错误")));
            }
        }
    }


    private void disable(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 禁用按钮
        PrintWriter out = resp.getWriter();
        String id = req.getParameter("id"); // 获取请求参数中的ID
        ObjectMapper objectMapper = new ObjectMapper(); // 创建ObjectMapper对象，用于将结果转换为JSON格式

        try {
            // 根据ID获取员工信息
            Employee employee = employeeService.getEmployeeById(Long.parseLong(id));

            // 将员工状态设置为禁用（0）
            employee.setStatus(0);

            // 保存更新后的员工信息
            boolean r = employeeService.save(employee, employee.getId());

            // 根据保存结果返回相应的结果
            if (r) {
                out.print(objectMapper.writeValueAsString(new Result(Result.SUCCESS, "禁用成功")));
            } else {
                out.print(objectMapper.writeValueAsString(new Result(Result.FAIL, "禁用失败")));
            }
        } catch (SQLException | CastResultException e) {
            // 捕获并处理可能发生的异常
            throw new RuntimeException(e);
        }

    }

    private void enable(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 启用按钮
        PrintWriter out = resp.getWriter();
        String id = req.getParameter("id"); // 获取请求参数中的ID
        ObjectMapper objectMapper = new ObjectMapper(); // 创建ObjectMapper对象，用于将结果转换为JSON格式

        try {
            // 根据ID获取员工信息
            Employee employee = employeeService.getEmployeeById(Long.parseLong(id));

            // 将员工状态设置为启用（1）
            employee.setStatus(1);

            // 保存更新后的员工信息
            boolean r = employeeService.save(employee, employee.getId());

            // 根据保存结果返回相应的结果
            if (r) {
                out.print(objectMapper.writeValueAsString(new Result(Result.SUCCESS, "启用成功")));
            } else {
                out.print(objectMapper.writeValueAsString(new Result(Result.FAIL, "启用失败")));
            }
        } catch (SQLException | CastResultException e) {
            // 捕获并处理可能发生的异常
            throw new RuntimeException(e);
        }

    }


    private void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> where = new HashMap<>();
        String name = req.getParameter("employeeName");
        String status = req.getParameter("employeeStatus");
        if (StrUtil.hasBlank(name)) {
            where.put("name", name);
        }
        if ((!StrUtil.hasBlank(status)) && (!status.equals("-1"))) {
            where.put("status", status);
        }
        int currentPage = 1;  // 当前也页页码
        int recordsPerPge = 5;
        if (!StrUtil.hasBlank(req.getParameter("page"))) {
            try {
                currentPage = Integer.parseInt(req.getParameter("page"));
            } catch (Exception e) {
            }
        }
        if (!StrUtil.hasBlank(req.getParameter("rows"))) {
            try {
                recordsPerPge = Integer.parseInt(req.getParameter("rows"));
            } catch (Exception e) {
            }
        }
        try {
            int total = employeeService.getTotal(where);
            ArrayList<Employee> employees = employeeService.list(where, currentPage, recordsPerPge);
            PrintWriter out = resp.getWriter();
            ObjectMapper objectMapper = new ObjectMapper();
            out.print(objectMapper.writeValueAsString(
                    new Result(Result.SUCCESS, "操作成功", total, employees)
            ));
        } catch (SQLException | CastResultException e) {
            handleException(req, resp, e);
        }
    }
}

