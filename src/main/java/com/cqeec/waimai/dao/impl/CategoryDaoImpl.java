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

    /**
     * 根据类别ID获取类别对象
     * 该方法使用数据库查询来获取指定ID的类别信息，并将查询结果转换为Category对象
     *
     * @param id 类别的唯一标识符
     * @return 对应ID的Category对象如果查询不到会返回null
     * @throws SQLException 如果数据库查询过程中发生错误
     * @throws CastResultException 如果查询结果转换为Category对象时发生错误
     */
    @Override
    public Category getCategoryById(long id) throws SQLException, CastResultException {
        // 执行数据库查询并获取结果，然后将结果转换为Category对象
        return DB.resultToObj(
                DB.query(
                        DB.getConnection(),
                        "select * from category where id = ?",
                        id
                ),
                Category.class
        );
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
        // 初始化SQL查询语句，从category表中计算匹配条件的记录数
        String sql = "select count(*) as c from category where 1=1";
        // 创建一个列表存储SQL查询参数值
        ArrayList<Object> values = new ArrayList<>();
        // 如果where中包含name字段，则添加模糊查询条件
        if (where.containsKey("name")){
            // 在SQL语句中添加名称模糊查询条件
            sql += " and name like ?";
            // 将模糊查询的名称参数添加到参数值列表中
            values.add("%" + where.get("name") + "%");
        }
        // 使用DB工具类执行查询，传入连接、SQL语句和参数值列表
        ResultSet rs = DB.query(
                DB.getConnection(),
                sql,
                values
        );
        // 处理查询结果，如果查询成功则返回记录数，否则返回0
        return rs.next() ? rs.getInt("c") : 0;
    }

    /**
     * 重写listAll方法，根据提供的条件获取所有分类
     * 此方法忽略传入的where参数，直接返回category表中的所有记录
     *
     * @param where 查询条件的映射，本方法中未使用该参数
     * @return 包含所有分类的ArrayList
     * @throws SQLException 如果数据库查询过程中发生错误
     * @throws CastResultException 如果结果集转换为Category列表时发生错误
     */
    @Override
    public ArrayList<Category> listAll(Map<String, Object> where) throws SQLException, CastResultException {
        // 获取分类管理类名
        String sql = "select * from category";
        // 执行查询并转换结果集为Category对象列表
        return DB.resultToList(
                DB.query(
                        DB.getConnection(),
                        sql
                ),
                Category.class
        );
    }

    /**
     * 删除指定ID的类别
     *
     * 该方法通过调用数据库操作，尝试删除一个具有特定ID的类别记录
     * 它使用了SQL的DELETE语句，并采取了参数化查询的方式，以防止SQL注入攻击
     *
     * @param id 要删除的类别的ID
     * @return 如果删除成功，则返回true；否则返回false
     * @throws SQLException 如果数据库操作失败，抛出此异常
     */
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
     * @param category 要添加的分类对象，包含需要插入到数据库的所有分类信息
     * @return 插入操作是否成功，成功返回true，否则返回false
     * @throws SQLException 如果数据库操作失败，抛出此异常
     */
    @Override
    public boolean insert(Category category) throws SQLException {
        // 创建sql语句
        String sql = "insert into category(name,type,sort,create_user,create_time,update_user,update_time) values(?,?,?,?,?,?,?)";
        // 执行插入操作，使用预编译防止SQL注入
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


    /**
     * 更新类别信息
     *
     * 此方法用于更新数据库中的类别记录它接受一个Category对象作为参数，
     * 该对象包含了需要更新的类别信息通过执行SQL语句来更新数据库中的相应记录
     *
     * @param category 要更新的类别对象，包含更新后的类别信息
     * @return 返回一个布尔值，表示更新操作是否成功
     * @throws SQLException 如果数据库操作失败，抛出此异常
     */
    @Override
    public boolean update(Category category) throws SQLException {
       // 准备更新类别的SQL语句
       String sql = "update category set name = ?,type = ?,sort = ?,update_user = ?,update_time = ? where id = ?";
       // 执行更新操作，传递类别信息和SQL语句
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
