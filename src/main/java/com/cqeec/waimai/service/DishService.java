package com.cqeec.waimai.service;

import com.cqeec.waimai.bean.Category;
import com.cqeec.waimai.bean.Dish;
import com.cqeec.waimai.exception.CastResultException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public interface DishService {
    int getTotal(Map<String, Object> where) throws SQLException;
    boolean delete(long id) throws SQLException, CastResultException;
    Dish getDishById(long id) throws SQLException, CastResultException;

    boolean save(Dish dish, long userId) throws SQLException, CastResultException;
    ArrayList<Dish> list(Map<String, Object> where, int currentPage, int recordsPerPage) throws SQLException, CastResultException;

    // 新增/修改
    boolean insert(Dish dish, long userId) throws SQLException ,CastResultException;
    boolean update(Dish dish, long userId) throws SQLException, CastResultException;

    // 停售/启售
    boolean startSale(long id, long userId) throws SQLException, CastResultException;
    boolean stopSale(long id, long userId) throws SQLException, CastResultException;
}
