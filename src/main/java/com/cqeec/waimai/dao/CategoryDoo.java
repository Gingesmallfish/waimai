package com.cqeec.waimai.dao;


import com.cqeec.waimai.bean.Category;
import com.cqeec.waimai.exception.CastResultException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public interface CategoryDoo {
    Category getCategoryById(long id) throws SQLException, CastResultException;
    ArrayList<Category> list(Map<String, Object> where, int page, int size) throws SQLException, CastResultException;
    int getTotal(Map<String, Object> where) throws SQLException;
    ArrayList<Category> listAll(Map<String, Object> where) throws SQLException, CastResultException;
    boolean delete(long id) throws SQLException;
    boolean insert(Category category) throws SQLException;
    boolean update(Category category) throws SQLException;

}
