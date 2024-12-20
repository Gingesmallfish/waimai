package com.cqeec.waimai.dao;

import com.cqeec.waimai.bean.Category;
import com.cqeec.waimai.bean.Dish;
import com.cqeec.waimai.exception.CastResultException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public interface DishDao {
    int getTotal(Map<String, Object> where) throws SQLException;

    Dish getDishById(long id) throws SQLException, CastResultException;

    boolean save(Dish dish,long userId) throws SQLException, CastResultException;

    ArrayList<Dish> list(Map<String, Object> where, int currentPage, int recordsPerPage) throws SQLException, CastResultException;

    boolean delete(long id) throws SQLException, CastResultException;
    boolean insert(Dish dish) throws SQLException;
    boolean update(Dish dish) throws SQLException;

}
