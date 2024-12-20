package com.cqeec.waimai.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.cqeec.waimai.bean.Dish;
import com.cqeec.waimai.bean.Employee;
import com.cqeec.waimai.exception.CastResultException;
import com.cqeec.waimai.service.DishService;
import com.cqeec.waimai.service.impl.DishServiceImpl;
import com.cqeec.waimai.util.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload2.core.DiskFileItem;
import org.apache.commons.fileupload2.core.DiskFileItemFactory;
import org.apache.commons.fileupload2.jakarta.servlet5.JakartaServletDiskFileUpload;


import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(value = "/dish_/*")
public class DishController extends BaseController {

    public DishService dishService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        dishService = new DishServiceImpl();
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String[] params = req.getPathInfo().split("/");
        if (params.length <= 1) {
            this.list(req, resp);
        }else {
            switch (params[1]) {
                case "list":
                    this.list(req, resp);
                    break;
                case "delete":
                    this.delete(req, resp);
                    break;
                case "stopsale":
                    this.stopSale(req, resp);
                    break;
                case "startsale":
                    this.startSale(req, resp);
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
     * 启动销售流程的私有方法
     * 该方法从HTTP请求中提取产品ID，并开始处理销售逻辑
     *
     * @param req HTTP请求对象，用于获取请求参数
     * @param resp HTTP响应对象，用于向客户端发送数据
     * @throws ServletException 如果Servlet遇到异常
     * @throws IOException 如果发生输入或输出异常
     */
    private void startSale(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取请求参数
        String id = req.getParameter("id");
        // 获取响应输出流
        PrintWriter out = resp.getWriter();
        // 创建ObjectMapper实例,
        ObjectMapper objectMapper = new ObjectMapper();

        // 检查菜品是否为空或包含空白字符串
        if (StrUtil.hasBlank(id)) {
            // 如果员工ID无效
            out.print(objectMapper.writeValueAsString(new Result(Result.FAIL,"菜品id不能为空")));
        } else {
            try {
                // 调用dishService的starSale方法，传入菜品ID和当前用户ID
                boolean r = dishService.startSale(Long.parseLong(id), ((Employee) req.getSession().getAttribute("user")).getId());
                if (r) {
                    // 如果操作成功，向响应中写入成功结果
                    out.print(objectMapper.writeValueAsString(new Result(Result.SUCCESS, "操作成功")));
                } else {
                    // 如果操作失败，向响应��写入失败结果
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
     * 停止销售操作
     *
     * 此方法用于处理停止销售的请求通过解析HTTP请求参数，执行停止销售的业务逻辑，
     * 并将处理结果封装到HTTP响应中
     *
     * @param req HTTP请求对象，包含停止销售所需的信息，如商品ID等
     * @param resp HTTP响应对象，用于向客户端返回停止销售操作的结果
     * @throws ServletException 如果在执行停止销售操作时发生错误
     * @throws IOException 如果在读取请求信息或向响应中写数据时发生I/O错误
     */
    private void stopSale(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取请求参数
        String id = req.getParameter("id");
        // 获取响应输出流
        PrintWriter out = resp.getWriter();
        // 创建ObjectMapper实例,
        ObjectMapper objectMapper = new ObjectMapper();
         Long userId = ((Employee) req.getSession().getAttribute("user")).getId();

        // 检查菜品是否为空或包含空白字符串
        if (StrUtil.hasBlank(id)) {
            // 如果员工ID无效
            out.print(objectMapper.writeValueAsString(new Result(Result.FAIL,"菜品id不能为空")));
        } else {
            try {
                // 调用dishService的startSale方法，传入菜品ID和当前用户ID
                boolean r = dishService.stopSale(Long.parseLong(id),userId);
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
 * 添加/修改菜品/上传图片
 *
 * @param req  HTTP请求对象，包含菜品信息和上传的图片
 * @param resp HTTP响应对象，用于向客户端返回处理结果
 * @throws ServletException 如果Servlet操作出错
 * @throws IOException      如果文件读写操作出错
 */
private void save(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String path = req.getServletContext().getRealPath("/upload/dish");
    File file = new File(path);
    if (!file.exists()) {
        file.mkdirs();
    }
    DiskFileItemFactory factory = DiskFileItemFactory.builder().get();
    JakartaServletDiskFileUpload upload = new JakartaServletDiskFileUpload(factory);
    List<DiskFileItem> fileItems = upload.parseRequest(req);

    PrintWriter out = resp.getWriter();
    ObjectMapper objectMapper = new ObjectMapper();

    String id = req.getParameter("id");
    String name = req.getParameter("_dishname");
    String img = req.getParameter("imageFile");
    String price = req.getParameter("price");
    String categoryId = req.getParameter("category_id");
    String code = req.getParameter("code");
    String dishSort = req.getParameter("dishSort");
    String description = req.getParameter("description");

    for (DiskFileItem item : fileItems) {
        if (!item.isFormField()) {
            String fileName = item.getName();
            if (!fileName.isEmpty()) {
                String suffix = fileName.substring(fileName.lastIndexOf("."));
                img = IdUtil.randomUUID() + suffix;
                File f = new File(file, img);
                item.write(f.toPath());
            }
        }
    }

    if (StrUtil.hasBlank(name) || StrUtil.hasBlank(price) || StrUtil.hasBlank(categoryId) || StrUtil.hasBlank(code) || StrUtil.hasBlank(dishSort)) {
        out.print(objectMapper.writeValueAsString(new Result(Result.FAIL, "菜品数据未填写完整")));
        return;
    }

    try {
        long dishId = StrUtil.isBlank(id) ? 0 : Long.parseLong(id.trim());
        double dishPrice = Double.parseDouble(price.trim());
        long dishCategoryId = Long.parseLong(categoryId.trim());
        int dishSortInt = Integer.parseInt(dishSort.trim());

        Dish dish = new Dish();
        dish.setId(dishId);
        dish.setName(name);
        dish.setImage(img);
        dish.setPrice(dishPrice);
        dish.setCategory_id(dishCategoryId);
        dish.setCode(code);
        dish.setSort(dishSortInt);
        dish.setDescription(description);

        Long userId = ((Employee) req.getSession().getAttribute("user")).getId();
        boolean r = dishService.save(dish, userId);
        if (r) {
            out.print(objectMapper.writeValueAsString(new Result(Result.SUCCESS, "添加成功")));
        } else {
            out.print(objectMapper.writeValueAsString(new Result(Result.FAIL, "添加失败")));
        }
    } catch (SQLException | CastResultException e) {
        handleException(req, resp, e);
        throw new RuntimeException(e);
    }
}

    /**
     * 获取查询
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1）获取响应的PrintWriter对象
        PrintWriter out = resp.getWriter();
        // 2）创建ObjectMapper对象用于将结果转为JSON格式
        ObjectMapper objectMapper = new ObjectMapper();
        // 初始化查询条件Map
        Map<String, Object> where = new HashMap<>();

        // 获取请求参数
        String name = req.getParameter("dishName");
        String status = req.getParameter("dishStatus");

        // 判断菜品名称不为空，则添加到查询条件中
        if (!StrUtil.hasBlank(name)) {
            where.put("name", name);
        }

        // 如果菜品状态不为空且不等于 -1，则添加到查询条件中
        if (!StrUtil.hasBlank(status) && !status.equals("-1")) {
            where.put("status", status);
        }

        // 设置分页参数
        int currentPage = 1;
        int recordsPerPage = 10;

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
                recordsPerPage = Integer.parseInt(req.getParameter("rows"));
            } catch (Exception e) {
                // 忽略异常，使用默认记录数
            }
        }

        try {
            // 获取符合条件的总记录数
            int total = dishService.getTotal(where);
            // 获取当前页的菜品列表
            ArrayList<Dish> dishes = dishService.list(where, currentPage, recordsPerPage);

            // 将查询结果以JSON格式输出
            out.print(objectMapper.writeValueAsString(
                    new Result(Result.SUCCESS, "查询成功", total, dishes)
            ));
        } catch (SQLException | CastResultException e) {
            // 处理异常
            handleException(req, resp, e);
        }
    }

    private void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    /**
     * 处理文件上传的私有方法
     * 该方法负责从HTTP请求中解析并保存上传的文件，并返回文件的访问路径
     *
     * @param req HTTP请求对象，包含上传文件的信息
     * @param resp HTTP响应对象，用于向客户端返回结果
     * @throws ServletException 如果Servlet操作出错
     * @throws IOException 如果文件读写操作出错
     */
//    private void upload(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        // 获取上传文件的保存目录的绝对路径
//        String path = req.getServletContext().getRealPath("/upload/dish");
//        File file = new File(path);
//        // 如果目录不存在，创建它
//        if (!file.exists()) {
//            file.mkdirs(); // 创建文件目录
//        }
//
//        // 创建一个DiskFileItemFactory工厂，用于构建上传组件
//        DiskFileItemFactory factory = DiskFileItemFactory.builder().get();
//        // 使用工厂创建上传组件
//        JakartaServletDiskFileUpload upload = new JakartaServletDiskFileUpload(factory);
//        // 解析请求，获取所有的文件项
//        List<DiskFileItem> fileItems = upload.parseRequest(req);
//
//        String filename = null;
//        // 遍历文件项
//        for (DiskFileItem item : fileItems) {
//            // 如果不是表单字段，而是上传的文件
//            if (!item.isFormField()) {
//                // 获取上传文件的文件名
//                String name = item.getName();
//                // 获取文件扩展名
//                String suffix = name.substring(name.lastIndexOf("."));
//                // 生成新的文件名，避免重名
//                filename = IdUtil.randomUUID() + suffix;
//                // 创建保存到服务器目录的文件对象
//                File f = new File(file, filename);
//                // 将上传的文件写入服务器目录
//                item.write(f.toPath());
//            }
//        }
//
//        // 创建一个结果Map，用于存储上传结果
//        Map<String, Object> result = new HashMap<>();
//        // 上传成功，错误码为0
//        result.put("error", 0);
//        // 存储文件的访问路径，用于回显
//        result.put("url", "upload/dish/" + filename);
//
//        // 将结果转换为JSON字符串并返回给客户端
//        resp.getWriter().print(new ObjectMapper().writeValueAsString(result));
//    }
}
