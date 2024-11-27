package com.cqeec.waimai.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.cqeec.waimai.bean.Employee;
import com.cqeec.waimai.controller.EmployeeController;
import com.cqeec.waimai.dao.EmployeeDao;
import com.cqeec.waimai.dao.impl.EmployeeDaoImpl;
import com.cqeec.waimai.exception.CastResultException;
import com.cqeec.waimai.service.EmployeeService;
import com.cqeec.waimai.util.Result;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;

/**
 *   员工模块业务逻辑类
 */
public class EmployeeServiceImpl implements EmployeeService {
    EmployeeDao employeeDao;
    public EmployeeServiceImpl() {
        employeeDao = new EmployeeDaoImpl();
    }



    @Override
    public boolean delete(long id) throws SQLException {
        return false;
    }

    @Override
    public Employee getEmployeeById(long id) throws SQLException, CastResultException {
        return employeeDao.getEmployeeById(id);
    }

    @Override
    public boolean updatePassword(Employee employee) throws SQLException {
        return employeeDao.update(employee);
    }

    /**
     *
     * @param employee
     * @param userId 操作者编号
     * @return
     * @throws SQLException
     * @throws CastResultException
     */
    @Override
    public boolean save(Employee employee,long userId) throws SQLException, CastResultException {
        // 验证员工登录账号是否有效-
        if (employeeDao.getEmployeeByUserName(employee.getId(),employee.getUsername()) == null) {
            if (employee.getId() == 0) {
                return insert(employee,userId);
            } else {
                return update(employee,userId);
            }
        } else {
            return false;
        }
    }

    public boolean insert(Employee employee,long userId) throws SQLException, CastResultException {
        // 将创建时间，创建人，跟新时间，跟新人存在到employee对象中
        employee.setCreate_user(userId);
        employee.setUpdate_user(userId);
        employee.setCreate_time(new Timestamp(System.currentTimeMillis()));
        employee.setUpdate_time(new Timestamp(System.currentTimeMillis()));
        return employeeDao.insert(employee);
    }

    public boolean update(Employee employee,long userId) throws SQLException, CastResultException {
        // 将创建时间，创建人，跟新时间，跟新人存在到employee对象中
        employee.setUpdate_user(userId);
        employee.setUpdate_time(new Timestamp(System.currentTimeMillis()));
        return employeeDao.update(employee);
    }


    /**
     * 禁用指定的员工账户
     * 此方法通过将员工的状态标记为禁用（状态码设为0）来实现账户的禁用功能
     * 它首先从数据库中获取指定ID的员工对象，然后将状态设置为禁用，
     * 最后调用update方法来更新数据库中的员工信息
     *
     * @param id 员工的唯一标识符，用于定位要禁用的员工账户
     * @param userId 操作此禁用动作的用户ID，用于记录操作日志或审计
     * @return 返回更新操作的结果，true表示成功禁用员工账户，false表示禁用失败
     * @throws SQLException 如果与数据库的交互过程中发生错误
     * @throws CastResultException 如果更新操作失败，抛出自定义异常
     */
    @Override
    public boolean disable(long id,long userId) throws SQLException, CastResultException {
        // 根据员工ID获取员工对象
        Employee employee = employeeDao.getEmployeeById(id);
        // 将员工状态设置为禁用（状态码0表示禁用）
        employee.setStatus(0);
        // 调用更新方法，将禁用状态持久化到数据库
        return update(employee,userId);
    }

    /**
     * 启用员工账户
     *
     * 该方法通过更新员工的状态来启用员工账户它首先根据员工ID获取员工对象，
     * 然后将员工的状态设置为启用状态（状态码为1），最后调用update方法来更新数据库中的员工信息
     *
     * @param id 员工ID，用于标识需要启用的员工
     * @param userId 操作用户ID，记录是谁启用了该员工账户
     * @return 返回更新操作是否成功
     * @throws SQLException 如果数据库操作失败
     * @throws CastResultException 如果转换操作结果异常
     */
    @Override
    public boolean enable(long id, long userId) throws SQLException, CastResultException {
        // 根据员工ID获取员工对象
        Employee employee = employeeDao.getEmployeeById(id);
        // 设置员工状态为启用状态
        employee.setStatus(1);
        // 更新数据库中的员工信息，并返回操作结果
        return update(employee, userId);
    }

    /**
     * 登录功能
     */
    @Override
    public Result login(String username, String password) throws SQLException, CastResultException {
        //调用数据访问层的方法通过username查询employee数据
        Employee employee = employeeDao.login(username);
        if(employee == null){
            return new Result(Result.FAIL,"用户名错误",employee);
        }else{
            //判断密码是否正确
            if(employee.getPassword().equals(SecureUtil.md5(password))){
                //判断用户状态
                if(employee.getStatus() == 1){
                    return new Result(Result.SUCCESS,"用户登录成功",employee);
                }else{
                    //用户为禁用状态
                    return new Result(Result.FAIL,"用户被禁用",employee);
                }
            }else{
                return new Result(Result.FAIL,"密码错误",employee);
            }
        }
    }

    /**
     * 根据给定的条件和分页参数获取员工列表
     * 此方法覆盖自上级类，旨在为员工管理功能提供分页查询能力
     *
     * @param where 一个映射，包含查询条件的关键字和值
     * @param currentPage 当前页码，用于确定返回结果的起始位置
     * @param recordsPerPage 每页记录数，用于限制返回结果的数量
     * @return 返回一个ArrayList，包含符合查询条件的员工对象列表
     * @throws SQLException 如果数据库操作失败，抛出此异常
     * @throws CastResultException 如果数据类型转换失败，抛出此异常
     */
    @Override
    public ArrayList<Employee> list(Map<String, Object> where, int currentPage, int recordsPerPage) throws SQLException, CastResultException {
        return employeeDao.list(where, currentPage, recordsPerPage);
    }

    @Override
    public int getTotal(Map<String, Object> where) throws SQLException {
        return employeeDao.getTotal(where);
    }
}
