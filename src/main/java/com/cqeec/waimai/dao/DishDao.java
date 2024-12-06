package com.cqeec.waimai.dao;

import com.cqeec.waimai.bean.Dish;
import com.cqeec.waimai.exception.CastResultException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public interface DishDao {
    int getTotal(Map<String, Object> where) throws SQLException;
    boolean delete(long id) throws SQLException, CastResultException;

    Dish getDishById(long id) throws SQLException, CastResultException;


    boolean save(Dish dish,long userId) throws SQLException, CastResultException;


    ArrayList<Dish> list(Map<String, Object> where, int currentPage, int recordsPerPage) throws SQLException, CastResultException;

    boolean startSale(long id,long userId) throws SQLException, CastResultException;

    boolean stopSale(long id,long userId) throws SQLException, CastResultException;
}
