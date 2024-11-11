package com.cqeec.waimai.service.impl;

import com.cqeec.waimai.bean.Employee;
import com.cqeec.waimai.dao.EmployeeDao;
import com.cqeec.waimai.dao.impl.EmployeeDaoImpl;
import com.cqeec.waimai.exception.CastResultException;
import com.cqeec.waimai.service.EmployeeService;
import com.cqeec.waimai.util.MD5;
import com.cqeec.waimai.util.Result;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

/**
 *  员工模块业务逻辑类
 */
public class EmployeeServiceImpl implements EmployeeService {

    public EmployeeServiceImpl() {
        employeeDao = new EmployeeDaoImpl();
    }

    EmployeeDao employeeDao;


    @Override
    public ArrayList<Employee> list(int page, int size) throws Exception {
        return null;
    }

    @Override
    public int getTotal() throws SQLException {
        return 0;
    }

    @Override
    public int getTotal(Map<String, Object> where) throws SQLException {
        return 0;
    }

    @Override
    public boolean delete(long id) throws SQLException {
        return false;
    }

    @Override
    public Employee getEmployeeById(long id) throws SQLException, CastResultException {
        return null;
    }

    @Override
    public boolean save(Employee employee) throws SQLException, CastResultException {
        return false;
    }

    @Override
    public boolean disable(long id) throws SQLException, CastResultException {
        return false;
    }

    @Override
    public boolean enable(long id) throws SQLException, CastResultException {
        return false;
    }

    /**
     * 登录功能
     * @param username 用户名
     * @param password 密码
     * @return
     * @throws SQLException
     * @throws CastResultException
     */
    @Override
    public Result login(String username, String password) throws SQLException, CastResultException {
        // 调用数据访问层方法通过username查询employee数据
        Employee employee = employeeDao.getEmployeeByName(username);
        if (employee == null) {
            return new Result(Result.FAIL,"用户名错误",employee);
        } else {
            // 判断密码是否正确
            if (employee.getPassword().equals(MD5.MD5Encrypt(password))) {
                // 判断用户状态
                if (employee.getStatus() ==1 ) {
                    return new Result(Result.SUCCESS, "用户登录成功", employee);
                } else  {
                    // 用户为禁用状态
                    return new Result(Result.FAIL, "用户被禁用", employee);
                }
            } else {
                return  new Result(Result.FAIL, "密码错误", employee);
            }
        }
    }

    @Override
    public ArrayList<Employee> list(Map<String, Object> params, int currentPage, int recordsPerPage) throws SQLException, CastResultException {
        return null;
    }
}
