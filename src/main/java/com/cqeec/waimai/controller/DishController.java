package com.cqeec.waimai.controller;


import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.cqeec.waimai.bean.Dish;
import com.cqeec.waimai.exception.CastResultException;
import com.cqeec.waimai.service.DishService;
import com.cqeec.waimai.util.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload2.core.DiskFileItemFactory;
import org.apache.commons.fileupload2.core.FileItem;
import org.apache.commons.fileupload2.jakarta.servlet5.JakartaServletFileUpload;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(value = "/dish_/*")
public class DishController extends BaseController {
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

    @Override
    public void init(ServletConfig config) throws ServletException {

    }

    private void startSale(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    private void stopSale(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    private void save(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    private void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    private void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }


}
