package com.cqeec.waimai.service.impl;

import com.cqeec.waimai.bean.Category;
import com.cqeec.waimai.bean.Employee;
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

    /**
     * 获取总数
     *
     * 此方法用于根据提供的查询条件计算总数它委托CategoryDoo类中的getTotal方法来执行实际的计算
     *
     * @param where 查询条件，以键值对形式提供，可能包含多个查询参数
     * @return 满足查询条件的总数
     * @throws SQLException 如果执行数据库操作时发生错误
     */
    @Override
    public int getTotal(Map<String, Object> where) throws SQLException {
        return categoryDoo.getTotal(where);
    }

    /**
     * 删除指定的类别数据对象
     * <p>
     * 此方法用于删除数据库中与给定ID关联的类别数据对象
     * 它通过调用categoryDoo的delete方法来实现删除操作
     *
     * @param id 要删除的类别数据对象的ID
     * @return 如果删除成功，则返回true；否则返回false
     * @throws SQLException 如果数据库操作失败，则抛出SQLException
     */
    @Override
    public boolean delete(long id) throws SQLException {
        // 删除
        return categoryDoo.delete(id);
    }

    /**
     * 根据类别ID获取类别信息
     * 此方法覆盖自CategoryDoo类，用于获取特定ID对应的类别信息
     * 主要用途是当需要根据类别ID检索类别详情时调用
     *
     * @param id 类别的唯一标识符
     * @return 返回一个Category对象，代表与给定ID关联的类别信息
     * @throws SQLException 当数据库操作失败时抛出此异常
     * @throws CastResultException 当转换数据库结果到Category对象失败时抛出此异常
     */
    @Override
    public Category getCategoryById(long id) throws SQLException, CastResultException {
        return categoryDoo.getCategoryById(id);
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

    /**
     * 插入类别信息到数据库
     * 此方法用于将类别对象插入到数据库中，同时记录创建和更新的信息
     *
     * @param category 要插入的类别对象，包含类别的相关信息
     * @param userId   当前操作的用户ID，用于记录创建和更新操作的责任人
     * @return 返回插入操作是否成功，true表示成功，false表示失败
     *
     * 方法通过设置创建时间和更新时间，以及创建和更新的用户ID，确保数据库中的信息完整可追溯
     */
    @Override
    public boolean insert(Category category, long userId) throws SQLException {
        // 设置创建者的ID
        category.setCreate_user(userId);
        // 设置更新者的ID
        category.setUpdate_user(userId);
        // 设置创建时间戳
        category.setCreate_time(new Timestamp(System.currentTimeMillis()));
        // 设置更新时间戳
        category.setUpdate_time(new Timestamp(System.currentTimeMillis()));
        // 调用数据操作对象执行插入操作并返回操作结果
        return categoryDoo.insert(category);
    }

    /**
     * 更新类别信息
     * 此方法用于更新数据库中的类别记录它将类别信息的更新操作委托给categoryDoo对象，
     * 并确保在更新前设置更新用户和更新时间
     *
     * @param category 要更新的类别对象，包含更新后的类别信息
     * @param userId 执行更新操作的用户ID
     * @return 如果更新成功，返回true；否则返回false
     * @throws SQLException 如果数据库操作失败，抛出此异常
     */
    @Override
    public boolean update(Category category, long userId) throws SQLException {
        // 设置类别ID，确保要更新的类别的唯一标识符正确
        category.setId(category.getId());
        // 设置类别名称，更新类别的名称信息
        category.setName(category.getName());
        // 设置类别类型，更新类别的类型信息
        category.setType(category.getType());
        // 设置类别排序，更新类别的排序信息
        category.setSort(category.getSort());
        // 设置更新用户，记录此次更新操作的执行者
        category.setUpdate_user(userId);
        // 设置更新时间，记录此次更新操作的时间
        category.setUpdate_time(new Timestamp(System.currentTimeMillis()));
        // 委托categoryDoo对象执行实际的数据库更新操作
        return categoryDoo.update(category);
    }

    /**
     * 根据给定的条件获取所有类别列表
     * 此方法覆盖自CategoryDao接口，用于从数据库中检索符合特定条件的所有Category对象
     * 它通过调用categoryDoo对象的listAll方法来实现这一功能
     *
     * @param where 一个映射，包含查询条件的关键字和值，用于限制查询结果
     * @return 返回一个ArrayList，其中包含所有满足查询条件的Category对象
     * @throws SQLException 如果数据库操作失败，抛出此异常
     * @throws CastResultException 如果将查询结果转换为Category对象时发生错误，抛出此异常
     */
    @Override
    public ArrayList<Category> listAll(Map<String, Object> where) throws SQLException, CastResultException {
        return categoryDoo.listAll(where);
    }


    /**
     * 保存分类信息和修改分类信息
     *
     * @param category 分类对象，包含要保存或修改的分类信息
     * @param userId    用户ID，用于记录执行保存或修改操作的用户
     * @return 操作成功返回true，否则返回false
     * @throws SQLException    操作数据库异常
     * @throws CastResultException  数据转换异常
     */
    @Override
    public boolean save(Category category, long userId) throws SQLException, CastResultException {
        // 判断分类对象的ID是否为0，若为0则执行插入操作，否则执行更新操作
        if (category.getId() == 0) {
            // 插入操作
            return insert(category, userId);
        } else {
            // 更新操作
            return update(category, userId);
        }
    }


}
