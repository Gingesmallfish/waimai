package com.cqeec.waimai.listener;

import com.cqeec.waimai.controller.BaseController;
import com.cqeec.waimai.util.DB;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.sql.SQLException;

@WebListener
public class ApplicationListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        if (!sce.getServletContext().getContextPath().equals("/")) {
            BaseController.contextPath = sce.getServletContext().getContextPath();
        } else {
            BaseController.contextPath = "";
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            if (DB.getConnection() != null) {
                try {
                    DB.getConnection().close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
