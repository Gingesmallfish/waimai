package com.cqeec.waimai.controller;

import cn.hutool.core.util.StrUtil;
import com.cqeec.waimai.bean.Category;
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

    /**
     * 初始化Servlet时设置类别服务
     *
     * @param config Servlet的配置对象，用于初始化Servlet
     * @throws ServletException 如果初始化过程中发生错误，抛出此异常
     *
     * 此方法在Servlet生命周期中被调用，用于初始化操作
     * 它创建了一个CategoryServiceImpl实例并将其赋值给categoryService变量
     * 这样做可能是为了在Servlet中使用categoryService提供的方法和服务
     */
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

    /**
     * 处理删除分类请求
     *
     * @param req  HttpServletRequest对象，用于获取请求参数
     * @param resp HttpServletResponse对象，用于输出响应内容
     * @throws ServletException 如果发生Servlet异常
     * @throws IOException      如果发生IO异常
     */
    private void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // 获取请求参数中的分类id
            long id = Long.parseLong(req.getParameter("id"));
            // 调用categoryService的delete方法来删除指定id的分类
            boolean r = categoryService.delete(id);
            // 如果删除成功，返回成功的Result对象，消息为“删除成功”
            if (r) {
                out.print(objectMapper.writeValueAsString(new Result(Result.SUCCESS, "删除成功")));
            } else {
                // 如果删除失败，返回失败的Result对象，消息为“删除失败”
                out.print(objectMapper.writeValueAsString(new Result(Result.FAIL, "删除失败")));
            }
        } catch (SQLException e) {
            // 处理SQL异常或类型转换异常
            handleException(req, resp, e);
            throw new RuntimeException(e);
        }
    }


    /**
     * 处理分类信息保存请求的方法
     * 该方法负责解析HTTP请求中的参数，验证数据完整性，并调用服务层方法进行数据的新增或修改
     *
     * @param req 用于获取请求参数的HttpServletRequest对象
     * @param resp 用于向客户端返回结果的HttpServletResponse对象
     * @throws ServletException 如果Servlet操作失败
     * @throws IOException 如果输入输出操作失败
     */
    private void save(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取用于向客户端输出响应内容的PrintWriter对象
        PrintWriter out = resp.getWriter();
        // 创建ObjectMapper对象，用于将Java对象转换为JSON格式以便在网络传输中使用
        ObjectMapper objectMapper = new ObjectMapper();

        // 获取请求参数，这些参数将用于新增或修改分类信息
        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String type = req.getParameter("type");
        String sort = req.getParameter("sort"); // 获取排序参数

        // 检查分类信息中的必填字段是否有空白值（为空或仅包含空格）
        // 如果有任何一个必填字段为空，将返回错误信息给客户端
        if (StrUtil.hasBlank(name)) {
            out.print(objectMapper.writeValueAsString(new Result(Result.FAIL, "分类数据未填写完整")));
            return;
        }

        try {
            // 创建一个Category对象，用于存储即将新增或修改的分类信息
            Category category = new Category();
            category.setName(name);
            category.setType(Long.parseLong(type));
            category.setSort(Integer.parseInt(sort)); // 设置排序

            if (StrUtil.hasBlank(id)) {
                // 新增操作
                category.setId(0);
                boolean result = categoryService.save(category, category.getId());
                out.print(objectMapper.writeValueAsString(new Result(result ? Result.SUCCESS : Result.FAIL, result ? "添加成功" : "添加失败")));
            } else {
                // 编辑操作
                category.setId(Long.parseLong(id));
                boolean result = categoryService.update(category, category.getId());
                out.print(objectMapper.writeValueAsString(new Result(result ? Result.SUCCESS : Result.FAIL, result ? "修改成功" : "修改失败")));
            }
        } catch (SQLException | CastResultException e) {
            // 异常处理，将请求转发到错误处理方法，并重新抛出运行时异常
            handleException(req, resp, e);
            throw new RuntimeException(e);
        }
    }



    /**
     * 列出所有分类信息
     *
     * 此方法负责处理列出所有分类信息的请求它从HTTP请求中获取参数，
     * 根据这些参数查询数据库，并将结果以JSON格式返回给客户端
     *
     * @param req HTTP请求对象，用于获取请求参数
     * @param resp HTTP响应对象，用于向客户端发送响应
     * @throws ServletException 如果Servlet操作失败
     * @throws IOException 如果输入/输出操作失败
     */
    private void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1）获取响应的PrintWriter对象
        PrintWriter out = resp.getWriter();
        // 2）创建ObjectMapper对象用于将结果转为JSON格式
        ObjectMapper objectMapper = new ObjectMapper();
        // 初始化查询条件Map
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

        // 如果请求中包含rows参数，则更新每页记录数
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

            // 将查询结果以JSON格式输出
            out.print(objectMapper.writeValueAsString(
                    new Result(Result.SUCCESS, "查询成功", total, categories)
            ));
        } catch (SQLException | CastResultException e) {
            // 处理异常
            handleException(req, resp, e);
        }
    }


    /**
     * 列出所有类别信息
     *
     * 该方法用于获取数据库中所有的类别信息，并将其以JSON格式返回给客户端
     * 它首先设置响应内容类型为文本/html，然后获取响应输出流
     * 使用ObjectMapper对象来帮助将类别信息转换为JSON格式字符串
     * 在处理过程中，如果遇到SQL异常或类型转换异常，将调用错误处理方法
     *
     * @param req 用于获取请求信息的HttpServletRequest对象
     * @param resp 用于向客户端发送响应的HttpServletResponse对象
     * @throws ServletException 如果Servlet运行出错
     * @throws IOException 如果在处理输入输出时发生错误
     */
    private void listAll(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取响应输出流，用于向客户端发送数据
        PrintWriter out = resp.getWriter();
        // 创建ObjectMapper对象，用于JSON序列化和反序列化
        ObjectMapper objectMapper = new ObjectMapper();
        // 创建一个空的条件映射，用于查询所有记录
        Map<String, Object> where = new HashMap<>();
        try {
            // 获取类别总数
            long total = categoryService.getTotal(where);
            System.out.println(total);
            // 获取所有类别信息
            ArrayList<Category> categories = categoryService.listAll(where);
            // 将查询结果以JSON格式返回给客户端
            out.print(objectMapper.writeValueAsString(new Result(Result.SUCCESS, "查询成功", categories)));
        } catch (SQLException | CastResultException e) {
            // 异常处理：调用处理异常的方法
            handleException(req, resp, e);
            throw new RuntimeException(e);
        }
    }
}


