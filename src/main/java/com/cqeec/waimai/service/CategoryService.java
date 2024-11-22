package com.cqeec.waimai.service;

import com.cqeec.waimai.bean.Category;
import com.cqeec.waimai.exception.CastResultException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public interface CategoryService {
    ArrayList<Category> list() throws SQLException, CastResultException;
    int getTotal(Map<String, Object> where) throws SQLException;
    boolean delete(long id) throws SQLException;

    Category getCategoryById(long id) throws SQLException, CastResultException;

    boolean save(Category category,long userId) throws SQLException, CastResultException;

    ArrayList<Category> list(Map<String, Object> where, int currentPage, int recordsPerPage) throws SQLException, CastResultException;
}
