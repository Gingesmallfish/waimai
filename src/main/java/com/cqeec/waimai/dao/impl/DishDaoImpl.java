package com.cqeec.waimai.dao.impl;

import com.cqeec.waimai.bean.Dish;
import com.cqeec.waimai.dao.DishDao;
import com.cqeec.waimai.exception.CastResultException;
import com.cqeec.waimai.util.DB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class DishDaoImpl implements DishDao {

    @Override
    public boolean save(Dish dish, long userId) throws SQLException, CastResultException {
        return DB.execute(
                DB.getConnection(), "INSERT INTO dish(`name`,`price`,`image`,`description`,`category_id`,`status`,`sort`,`is_deleted`,`create_time`," +
                        "`update_time`,`create_user`,`update_user`) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)",
                dish.getName(), dish.getPrice(), dish.getImage(), dish.getDescription(), dish.getCategory_id(), dish.getStatus(),
                dish.getSort(), dish.getIs_deleted(), dish.getCreate_time(), dish.getUpdate_time(),dish.getCreate_user(),dish.getUpdate_user()
        );
    }

    @Override
    public int getTotal(Map<String, Object> where) throws SQLException {
        String sql = "select count(*) as c from dish where 1=1";
        ArrayList<Object> values = new ArrayList<>();

        //如果where中包含name字段，则添加模糊查询条件
        if (where.containsKey("name")) {
            sql += " and name like ?";
            values.add("%" + where.get("name") + "%");
        }

        // 执行查询并返回结果
        ResultSet rs = DB.query(
                DB.getConnection(),
                sql,
                values
        );
        return rs.next() ? rs.getInt("c") : 0; // 这里用的是三元运算符
    }

    @Override
    public Dish getDishById(long id) throws SQLException, CastResultException {
        return DB.resultToObj(
                DB.query(
                        DB.getConnection(),
                        "select * from dish where id = ?",
                        id
                ),
                Dish.class
        );
    }

    @Override
    public ArrayList<Dish> list(Map<String, Object> where, int currentPage, int recordsPerPage) throws SQLException, CastResultException {
        ArrayList<Object> values = new ArrayList<>();
        String sql = "SELECT * FROM dish WHERE 1=1"; // 修正表名为 dish 并添加 from 关键字

        if (where.containsKey("name")) {
            sql += " AND name LIKE ?";
            values.add("%" + where.get("name") + "%"); // 修正值的添加方式
        }

        // 如果where中包含status字段，则添加状态查询条件
        if (where.containsKey("status")) {
            sql += " AND status = ?";
            values.add(where.get("status"));
        }

        if (where.containsKey("is_deleted")) {
            sql += " AND is_deleted = ?";
            values.add(where.get("is_deleted"));
        }

        sql += " ORDER BY create_time LIMIT ?, ?";
        values.add((currentPage - 1) * recordsPerPage);
        values.add(recordsPerPage);

        return DB.resultToList(
                DB.query(
                        DB.getConnection(),
                        sql,
                        values
                ),
                Dish.class
        );

    }


    @Override
    public boolean delete(long id) throws SQLException, CastResultException {
        return DB.execute(
                DB.getConnection(),
                "delete from dish where id = ?",
                id
        );
    }

    @Override
    public boolean insert(Dish dish) throws SQLException {
        return DB.execute(
                DB.getConnection(), "INSERT INTO dish(`name`,`price`,`image`,`description`,`category_id`,`status`,`sort`,`is_deleted`,`create_time`," +
                        "`update_time`,`create_user`,`update_user`) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)",
                dish.getName(), dish.getPrice(), dish.getImage(), dish.getDescription(), dish.getCategory_id(), dish.getStatus(),
                dish.getSort(), dish.getIs_deleted(), dish.getCreate_time(), dish.getUpdate_time(),dish.getCreate_user(),dish.getUpdate_user()
        );
    }

    @Override
    public boolean update(Dish dish) throws SQLException {
        return DB.execute(
                DB.getConnection(), "UPDATE dish SET name = ?, price = ?, image = ?, description = ?, category_id = ?, status = ?, sort = ?, " +
                        "is_deleted = ?, create_time = ?, update_time = ?, create_user = ?, update_user = ? WHERE id = ?",
                dish.getName(), dish.getPrice(), dish.getImage(), dish.getDescription(), dish.getCategory_id(), dish.getStatus(),
                dish.getSort(), dish.getIs_deleted(), dish.getCreate_time(), dish.getUpdate_time(), dish.getCreate_user(),dish.getCreate_user(), dish.getId()
        )   ;
    }


}
