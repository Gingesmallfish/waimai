package com.cqeec.waimai.service.impl;

import com.cqeec.waimai.bean.Category;
import com.cqeec.waimai.dao.CategoryDoo;
import com.cqeec.waimai.dao.impl.CategoryDaoImpl;
import com.cqeec.waimai.exception.CastResultException;
import com.cqeec.waimai.service.CategoryService;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;


public class CategoryServiceImpl implements CategoryService {

    CategoryDoo categoryDoo;

    public CategoryServiceImpl() {
        categoryDoo = new CategoryDaoImpl();
    }

    /**
     * 重写list方法，根据名称获取Category对象的ArrayList
     * 此方法旨在通过特定名称查询相关的类别信息，提供一种根据名称筛选类别的手段
     *
     * @param name 要查询的类别名称，用于筛选符合条件的Category对象
     * @return 返回一个包含符合条件的Category对象的ArrayList，如果查询不到任何结果，返回null
     *
     * @throws SQLException 如果在执行数据库操作时发生错误，抛出此异常
     * @throws CastResultException 如果在转换查询结果时发生错误，抛出此异常
     */
    @Override
    public ArrayList<Category> list(String name) throws SQLException, CastResultException {
       // 创建一个映射，用于存储查询条件，键为"name"，值为方法参数name
       Map<String,Object> where = Map.of("name", name);
       // 调用categoryDoo的list方法，传入查询条件，以及分页参数1和Integer.MAX_VALUE，以获取所有符合条件的Category对象
       ArrayList<Category> categories = categoryDoo.list(where, 1,Integer.MAX_VALUE);

       // 返回查询结果
       return categories;
    }

    @Override
    public int getTotal(Map<String, Object> where) throws SQLException {
        return categoryDoo.getTotal(where);
    }

    @Override
    public boolean delete(long id) throws SQLException {
        return false;
    }

    @Override
    public Category getCategoryById(long id) throws SQLException, CastResultException {
        return null;
    }

    /**
     * 保存分类信息
     * @param category
     * @param userId
     * @return
     * @throws SQLException
     * @throws CastResultException
     */
    @Override
    public boolean save(Category category, long userId) throws SQLException, CastResultException {
       category.setCreate_user(userId);
       category.setUpdate_user(userId);
       category.setCreate_time(new Timestamp(System.currentTimeMillis()));
       category.setUpdate_time(new Timestamp(System.currentTimeMillis()));
       return categoryDoo.insert(category);
    }

    /**
     * 根据给定的条件获取分类列表
     * 此方法重写了父类或接口的方法，用于实现获取分类列表的功能
     * 它根据提供的查询条件、当前页码和每页记录数来获取数据
     *
     * @param where 查询条件的映射，用于指定查询的条件
     * @param currentPage 当前页码，用于分页查询
     * @param recordsPerPage 每页记录数，用于限制每页返回的记录数量
     * @return 返回一个ArrayList，其中包含符合查询条件的Category对象
     * @throws SQLException 如果数据库操作失败，抛出此异常
     * @throws CastResultException 如果数据类型转换失败，抛出此异常
     */
    @Override
    public ArrayList<Category> list(Map<String, Object> where, int currentPage, int recordsPerPage) throws SQLException, CastResultException {
        return categoryDoo.list(where, currentPage, recordsPerPage);
    }
}
