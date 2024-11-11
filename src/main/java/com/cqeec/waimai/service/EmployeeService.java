package com.cqeec.waimai.service;

import com.cqeec.waimai.bean.Employee;
import com.cqeec.waimai.exception.CastResultException;
import com.cqeec.waimai.util.Result;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public interface EmployeeService {

    ArrayList<Employee> list(int page, int size) throws Exception;
    int getTotal() throws SQLException;
    int getTotal(Map<String, Object>where) throws SQLException;
    boolean delete(long id) throws SQLException;

    Employee getEmployeeById(long id) throws SQLException, CastResultException;


    boolean save(Employee employee) throws SQLException, CastResultException;

    boolean disable(long id) throws SQLException, CastResultException;

    boolean enable(long id) throws SQLException, CastResultException;

    Result login(String username, String password) throws SQLException, CastResultException;

    ArrayList<Employee> list(Map<String, Object> params, int currentPage, int recordsPerPage) throws SQLException, CastResultException;
}
