package com.cqeec.waimai.controller;


import cn.hutool.core.util.StrUtil;
import com.cqeec.waimai.bean.Category;
import com.cqeec.waimai.bean.Employee;
import com.cqeec.waimai.exception.CastResultException;
import com.cqeec.waimai.service.CategoryService;
import com.cqeec.waimai.service.impl.CategoryServiceImpl;
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


@WebServlet(value = "/category_/*")
public class CategoryController extends BaseController {

    public CategoryService categoryService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        categoryService = new CategoryServiceImpl();
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
                case "listAll":
                    this.listAll(req, resp);
                    break;
                case "delete":
                    this.delete(req, resp);
                    break;
                case "save":
                    this.save(req, resp);
                    break;
                default:
                    handleException(req, resp, 404, "无法找到请求的资源");
            }
        }
    }

    private void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    private void save(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    private void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }


    private void listAll(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1）获取响应的PrintWriter对象
        PrintWriter out = resp.getWriter();
        // 2）创建ObjectMapper对象用于将结果转为JSON格式
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> where = new HashMap<>();
        // 获取请求参数
        String name = req.getParameter("categoryName");
        String type = req.getParameter("categoryType");
        // 判断员工姓名不为空，则添加到查询条件中
        if (StrUtil.hasBlank(name)) {
            where.put("name", name);
        }
        // 如果员工状态不为空不等于 -1，则添加假查询条件中
        if ((!StrUtil.hasBlank(type)) && (!type.equals("-1"))) {
            where.put("type", type);
        }
        // 设置分页参数
        int currentPage = 1;
        int recordsPerPge = 10;
        // 如果请求中包含page参数，则更新当前页码
        if (!StrUtil.hasBlank(req.getParameter("page"))) {
            try {
                currentPage = Integer.parseInt(req.getParameter("page"));
            } catch (Exception e) {
                // 忽略异常，使用默认页码
            }
        }

        if (!StrUtil.hasBlank(req.getParameter("rows"))) {
            try {
                recordsPerPge = Integer.parseInt(req.getParameter("rows"));
            } catch (Exception e) {
                // 忽略异常，使用默认记录数
            }
        }
        try {
            // 获取符合条件的总记录数
            int total = categoryService.getTotal(where);
            // 获取当前页的员工列表
            ArrayList<Category> categories = categoryService.list(where, currentPage, recordsPerPge);

            // 输出结果
            out.print(objectMapper.writeValueAsString(
                    new Result(Result.SUCCESS, "查询成功", total, categories)
            ));
        } catch (SQLException | CastResultException e) {
            // 处理异常
            handleException(req, resp, e);
        }
    }
}


