package com.cqeec.waimai.dao;

import com.cqeec.waimai.bean.Employee;
import com.cqeec.waimai.exception.CastResultException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public interface EmployeeDao {


    Employee getEmployeeById(long var1) throws SQLException, CastResultException;

    /**
     *  实现用户登录
     * @param username 用户名
     * @return employee
     * @throws SQLException
     * @throws CastResultException
     */
    Employee getEmployeeByName(String username) throws SQLException, CastResultException;
    ArrayList<Employee> list() throws SQLException, CastResultException;
    ArrayList<Employee> list(int page,int size) throws SQLException, CastResultException;
    ArrayList<Employee> list(Map<String,Object> where,int page,int size) throws SQLException, CastResultException;
    int getTotal() throws SQLException;
    int getTotal(Map<String,Object> where) throws SQLException;

    boolean delete(long id) throws SQLException;

    boolean insert(Employee employee) throws SQLException;
    boolean update(Employee employee) throws SQLException;


}
