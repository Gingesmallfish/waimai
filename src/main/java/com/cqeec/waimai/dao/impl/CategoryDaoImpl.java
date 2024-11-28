package com.cqeec.waimai.dao.impl;

import com.cqeec.waimai.bean.Category;
import com.cqeec.waimai.dao.CategoryDoo;
import com.cqeec.waimai.exception.CastResultException;
import com.cqeec.waimai.util.DB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

/**
 * 分类管理
 */
public class CategoryDaoImpl implements CategoryDoo {

    @Override
    public Category getCategoryById(long id) throws SQLException, CastResultException {
        return null;
    }

    @Override
    public Category getCategoryByName(String name) throws SQLException, CastResultException {
       return null;
    }

    /**
     * 根据条件查询分类列表
     *
     * @param where 查询条件，包含可选的名称和类型字段
     * @param page 页码，用于分页查询
     * @param size 每页大小，用于分页查询
     * @return 返回查询到的分类列表
     * @throws SQLException 当数据库查询操作失败时抛出此异常
     * @throws CastResultException 当结果集转换操作失败时抛出此异常
     */
    @Override
    public ArrayList<Category> list(Map<String, Object> where, int page, int size) throws SQLException, CastResultException {
       String sql = "select * from category where 1=1";
       // 创建一个列表
        ArrayList<Object> values = new ArrayList<>();
        //如果查询中包含名称字段，则添加名称模糊查询条件
        if (where.containsKey("name")){
            sql += " and name like ?";
            values.add("%" + where.get("name") + "%");

        }

        // 如果查询条件中包含类型字段，则添加类型精确查询条件
        if (where.containsKey("type")){
            sql += " and type = ?";
            values.add(where.get("type"));
        }

        // 添加排序条件
        sql += " order by create_time asc limit ?,?";
        values.add((page - 1) * size);
        values.add(size);
        // 执行查询并转换结果集为Category对象列表
        return DB.resultToList(
                DB.query(
                        DB.getConnection(),
                        sql,
                        values
                ),
                Category.class
        );
    }

    /**
     * 根据条件获取类别总数
     *
     * @param where 包含查询条件的映射，可能包括名称等条件
     * @return 类别总数
     * @throws SQLException 如果数据库查询过程中发生错误
     */
    @Override
    public int getTotal(Map<String, Object> where) throws SQLException {
        String sql = "select count(*) as c from category where 1=1";
        ArrayList<Object> values = new ArrayList<>();
        // 如果where中包含name字段，则添加模糊查询条件
        if (where.containsKey("name")){
            sql += " and name like ?";
            values.add("%" + where.get("name") + "%");
        }
        // 执行查询并返回结果
        ResultSet rs = DB.query(
                DB.getConnection(),
                sql,
                values
        );
        return rs.next() ? rs.getInt("c") : 0;
    }

    @Override
    public boolean delete(long id) throws SQLException {
       return DB.execute(
               DB.getConnection(),
               "delete from category where id = ?",
               id
       );
    }

    /**
     * 添加分类
     * @param category
     * @return
     * @throws SQLException
     */
    @Override
    public boolean insert(Category category) throws SQLException {
       // 创建sql语句
        String sql = "insert into category(name,type,sort,create_user,create_time,update_user,update_time) values(?,?,?,?,?,?,?)";
        return DB.execute(
                DB.getConnection(),
                sql,
                category.getName(),
                category.getType(),
                category.getSort(),
                category.getCreate_user(),
                category.getCreate_time(),
                category.getUpdate_user(),
                category.getUpdate_time()
        );
    }

    @Override
    public boolean update(Category category) throws SQLException {
       String sql = "update category set name = ?,type = ?,sort = ?,update_user = ?,update_time = ? where id = ?";
       return DB.execute(
               DB.getConnection(),
               sql,
               category.getName(),
               category.getType(),
               category.getSort(),
               category.getUpdate_user(),
               category.getUpdate_time(),
               category.getId()
       );
    }
}
