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

    @Override
    public boolean disable(long id,long userId) throws SQLException, CastResultException {
        int status = 0;
        return employeeDao.disable(id,status,userId);
    }

    @Override
    public boolean enable(long id,long userId) throws SQLException, CastResultException {
        int status = 1;
        return employeeDao.disable(id,status,userId);
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

    @Override
    public ArrayList<Employee> list(Map<String, Object> where, int currentPage, int recordsPerPage) throws SQLException, CastResultException {
        return employeeDao.list(where, currentPage, recordsPerPage);
    }

    @Override
    public int getTotal(Map<String, Object> where) throws SQLException {
        return employeeDao.getTotal(where);
    }
}
