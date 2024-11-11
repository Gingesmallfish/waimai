package com.cqeec.waimai.dao;

import com.cqeec.waimai.bean.Dish;
import com.cqeec.waimai.exception.CastResultException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public interface DishDao {

    Dish getDishById(long id) throws SQLException, CastResultException;

    ArrayList<Dish> list() throws SQLException, CastResultException;
    ArrayList<Dish> list(int page,int size) throws SQLException, CastResultException;
    ArrayList<Dish> list(Map<String,Object> where,int page,int size) throws SQLException, CastResultException;
    int getTotal() throws SQLException;
    int getTotal(Map<String,Object> where) throws SQLException;

    boolean delete(long id) throws SQLException;

    boolean insert(Dish dish) throws SQLException;
    boolean update(Dish dish) throws SQLException;

    Dish getDishByName(String name) throws SQLException, CastResultException;
}
