package com.cqeec.waimai.dao.impl;

import com.cqeec.waimai.bean.Dish;
import com.cqeec.waimai.dao.DishDao;
import com.cqeec.waimai.exception.CastResultException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class DishDaoImpl implements DishDao {
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
        return null;
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
