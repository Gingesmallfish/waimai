package com.cqeec.waimai.dao;

import com.cqeec.waimai.bean.Employee;
import com.cqeec.waimai.exception.CastResultException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public interface EmployeeDao {

    Employee getEmployeeById(long id) throws SQLException, CastResultException;
    Employee getEmployeeByUserName(long id,String username) throws SQLException, CastResultException;
    Employee login(String username) throws SQLException, CastResultException;
    ArrayList<Employee> list(Map<String,Object> where,int page,int size) throws SQLException, CastResultException;
    int getTotal(Map<String,Object> where) throws SQLException;

    boolean delete(long id) throws SQLException;


    boolean insert(Employee employee) throws SQLException;
    boolean update(Employee employee) throws SQLException;

}
