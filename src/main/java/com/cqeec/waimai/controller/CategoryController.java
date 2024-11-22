package com.cqeec.waimai.controller;

import cn.hutool.core.util.StrUtil;
import com.cqeec.waimai.bean.Category;
import com.cqeec.waimai.bean.Employee;
import com.cqeec.waimai.exception.CastResultException;
import com.cqeec.waimai.service.CategoryService;
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


    @Override
    public void init(ServletConfig config) throws ServletException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String[] params = req.getPathInfo().split("/");
        if (params.length <= 1) {
            this.list(req, resp);
        }else{
            switch (params[1]) {
                case "listAll":
                    this.listAll(req,resp);
                    break;
                case "list":
                    this.list(req, resp);
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
        
    }
}

