package com.cqeec.waimai.dao;


import com.cqeec.waimai.bean.Category;
import com.cqeec.waimai.exception.CastResultException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public interface CategoryDao {


    Category getCategoryById(long id) throws SQLException, CastResultException;

    ArrayList<Category> list() throws SQLException, CastResultException;
    ArrayList<Category> list(int page,int size) throws SQLException, CastResultException;
    ArrayList<Category> list(Map<String,Object> where,int page,int size) throws SQLException, CastResultException;
    int getTotal() throws SQLException;
    int getTotal(Map<String,Object> where) throws SQLException;

    boolean delete(long id) throws SQLException;

    boolean insert(Category category) throws SQLException;
    boolean update(Category category) throws SQLException;


    Category getCategoryByName(String name) throws SQLException, CastResultException;
}
