package com.cqeec.waimai.service.impl;

import com.cqeec.waimai.bean.Dish;
import com.cqeec.waimai.exception.CastResultException;
import com.cqeec.waimai.service.DishService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class DishServiceImpl implements DishService {
    DishService dishService;
    public DishServiceImpl() {
        dishService=new DishServiceImpl();
    }
    @Override
    public int getTotal(Map<String, Object> where) throws SQLException {
        return 0;
    }

    @Override
    public boolean delete(long id) throws SQLException, CastResultException {
        return false;
    }

    @Override
    public Dish getDishById(long id) throws SQLException, CastResultException {
        return null;
    }

    @Override
    public boolean save(Dish dish, long userId) throws SQLException, CastResultException {
        return false;
    }

    @Override
    public ArrayList<Dish> list(Map<String, Object> where, int currentPage, int recordsPerPage) throws SQLException, CastResultException {
        ArrayList<Dish> dishes = dishService.list(where,currentPage,recordsPerPage);
        for (Dish dish : dishes) {
            dish.setCategory(dishService.getDishById(dish.getId()).getCategory());
        }
       return dishes;
    }

    @Override
    public boolean startSale(long id, long userId) throws SQLException, CastResultException {
        return false;
    }

    @Override
    public boolean stopSale(long id, long userId) throws SQLException, CastResultException {
        return false;
    }
}
