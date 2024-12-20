package com.cqeec.waimai.service.impl;

import com.cqeec.waimai.bean.Dish;
import com.cqeec.waimai.dao.DishDao;
import com.cqeec.waimai.dao.impl.CategoryDaoImpl;
import com.cqeec.waimai.dao.impl.DishDaoImpl;
import com.cqeec.waimai.exception.CastResultException;
import com.cqeec.waimai.service.DishService;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;

public class DishServiceImpl implements DishService {
    DishDao dishDao;
    public DishServiceImpl() {
        dishDao = new DishDaoImpl();
    }

    @Override
    public int getTotal(Map<String, Object> where) throws SQLException {
        return dishDao.getTotal(where);
    }

    @Override
    public boolean delete(long id) throws SQLException, CastResultException {
        return dishDao.delete(id);
    }

   

    /**
     * 根据菜肴ID获取菜肴详情
     * 此方法用于从数据源获取特定菜肴的信息它依赖于dishDao接口的实现，
     * 该实现负责与底层数据库或其他数据存储交互
     *
     * @param id 要查询的菜肴的唯一标识符
     * @return 返回一个Dish对象，包含所请求的菜肴的所有信息
     * @throws SQLException        当与数据库的交互导致SQL异常时抛出
     * @throws CastResultException 当将查询结果转换为Dish对象时发生错误时抛出
     */
    @Override
    public Dish getDishById(long id) throws SQLException, CastResultException {
        return dishDao.getDishById(id);
    }

    /**
     * 保存菜品信息根据菜品ID判断是插入新菜品还是更新现有菜品
     *
     * @param dish 菜品对象，包含菜品信息
     * @param userId 用户ID，用于记录操作者信息
     * @return 返回操作结果，true表示成功，false表示失败
     * @throws SQLException 当数据库操作失败时抛出此异常
     * @throws CastResultException 当操作结果转换失败时抛出此异常
     */
    @Override
    public boolean save(Dish dish, long userId) throws SQLException, CastResultException {
        // 判断分类对象ID是否为0，若为0则执行插入操作，否则执行更新操作
        if (dish.getId() == 0) {
            // 插入操作
            return insert(dish, userId);
        } else {
            // 更新操作
            return update(dish, userId);
        }

    }

    /**
     * 根据条件列出菜��列表
     *
     * @param where          包含查询条件的映射表，用于指定查询参数
     * @param currentPage    当前页码，用于分页查询
     * @param recordsPerPage 每页记录数，用于限定查询结果数量
     * @return 返回一个ArrayList，其中包含符合查询条件的Dish对象
     * @throws SQLException        当数据库操作失败时抛出此异常
     * @throws CastResultException 当转换查询结果失败时抛出此异常
     *                             此方法通过调用dishService的list方法获取符合查询条件的菜品列表，
     *                             然后为每个菜品对象设置其关联的类别信息，以便于后续处理和显示
     */
    @Override
    public ArrayList<Dish> list(Map<String, Object> where, int currentPage, int recordsPerPage) throws SQLException, CastResultException {
        // 获取符合查询条件的菜品列表
        ArrayList<Dish> dishes = dishDao.list(where, currentPage, recordsPerPage);
        // 为每个菜品对象设置类别信息
        for (Dish dish : dishes) {
            // 从数据库中获取当前菜品的详细信息，并设置其类别
            dish.setCategory(new CategoryDaoImpl().getCategoryById(dish.getCategory_id()));
        }
        // 返回设置好类别信息的菜品列表
        return dishes;
    }


    @Override
    public boolean insert(Dish dish, long userId) throws SQLException {
        // 设置菜品的创建者ID
        dish.setCreate_user(userId);

        // 设置菜品的更新者ID
        dish.setUpdate_user(userId);

        // 设置菜品的创建时间
        dish.setCreate_time(new Timestamp(System.currentTimeMillis()));

        // 设置菜品的更新时间
        dish.setUpdate_time(new Timestamp(System.currentTimeMillis()));

        // 调用 DishDao 的插入方法
        return dishDao.insert(dish);
    }

    @Override
    public boolean update(Dish dish, long userId) throws SQLException, CastResultException {
        dish.setUpdate_user(userId);
        dish.setUpdate_time(new Timestamp(System.currentTimeMillis()));
        return dishDao.update(dish);
    }


    /**
     * 启用菜品
     *
     * @param id
     * @param userId
     * @return
     * @throws SQLException
     * @throws CastResultException
     */
    @Override
    public boolean startSale(long id, long userId) throws SQLException, CastResultException {
        Dish dish = dishDao.getDishById(id);
        dish.setStatus(1);
        return update(dish, userId);
    }

    /**
     * 停用菜品
     *  @param id 商品的唯一标识码，用于指定要销售的商品。
     *  @param userId 用户的唯一标识符，用于标识发起销售的用户。
     *  @return 返回一个布尔值，该值指示销售结果。True 表示销售成功，而 false 表示销售失败。
     *  @throws SQLException 如果在销售过程中数据库操作出现错误。
     *  @throws CastResultException 如果销售结果处理有错误。
     */
    @Override
    public boolean stopSale(long id, long userId) throws SQLException, CastResultException {
        Dish dish = dishDao.getDishById(id);
        dish.setStatus(0);
       return update(dish, userId);
    }
}
