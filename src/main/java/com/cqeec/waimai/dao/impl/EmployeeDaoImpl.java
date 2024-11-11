package com.cqeec.waimai.dao.impl;

import com.cqeec.waimai.bean.Employee;
import com.cqeec.waimai.dao.EmployeeDao;
import com.cqeec.waimai.exception.CastResultException;
import com.cqeec.waimai.util.DB;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class EmployeeDaoImpl implements EmployeeDao {


    @Override
    public Employee getEmployeeById(long var1) throws SQLException, CastResultException {
        return null;
    }

    @Override
    public Employee getEmployeeByName(String username) throws SQLException, CastResultException {

        Employee employee =  DB.resultToObj(
                DB.query(DB.getConnection(), "select * from employee where username = ? ",username),
                Employee.class
        );
       return employee;
    }

    @Override
    public ArrayList<Employee> list() throws SQLException, CastResultException {
        return null;
    }

    @Override
    public ArrayList<Employee> list(int page, int size) throws SQLException, CastResultException {
        return null;
    }

    @Override
    public ArrayList<Employee> list(Map<String, Object> where, int page, int size) throws SQLException, CastResultException {
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
    public boolean insert(Employee employee) throws SQLException {
        return false;
    }

    @Override
    public boolean update(Employee employee) throws SQLException {
        return false;
    }
}
