package com.cqeec.waimai.service;

import com.cqeec.waimai.bean.Dish;
import com.cqeec.waimai.exception.CastResultException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public interface DishService {

    ArrayList<Dish> list(int page, int size) throws Exception;
    int getTotal() throws SQLException;
    int getTotal(Map<String, Object>where) throws SQLException;
    boolean delete(long id) throws SQLException, CastResultException;

    Dish getById(long id) throws SQLException, CastResultException;


    boolean save(Dish dish) throws SQLException, CastResultException;


    ArrayList<Dish> list(Map<String, Object> params, int currentPage, int recordsPerPage) throws SQLException, CastResultException;

    boolean startSale(long id) throws SQLException, CastResultException;

    boolean stopSale(long id) throws SQLException, CastResultException;
}
