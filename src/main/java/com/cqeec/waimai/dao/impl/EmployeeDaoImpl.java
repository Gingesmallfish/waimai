package com.cqeec.waimai.dao.impl;

import com.cqeec.waimai.bean.Employee;
import com.cqeec.waimai.dao.EmployeeDao;
import com.cqeec.waimai.exception.CastResultException;
import com.cqeec.waimai.util.DB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class EmployeeDaoImpl implements EmployeeDao {
    @Override
    public Employee getEmployeeById(long id) throws SQLException, CastResultException {
        return DB.resultToObj(
                DB.query(
                        DB.getConnection(),
                        "select * from employee where id = ?",
                        id
                ),
                Employee.class
        );
    }

    @Override
    public Employee getEmployeeByUserName(long id, String username) throws SQLException, CastResultException {
        if (id == 0) {  // 新增
            return DB.resultToObj(
              DB.query(
                      DB.getConnection(),
                      "select * from employee where username = ?",
                      username
              ),
              Employee.class
            );
        } else { // 修改
            return DB.resultToObj(
                    DB.query(
                            DB.getConnection(),
                            "select * from employee where username = ? and id != ?",
                            username,
                            id
                    ),
                    Employee.class
            );
        }
    }

    @Override
    public Employee login(String username) throws SQLException, CastResultException {
        Employee employee = DB.resultToObj(
                DB.query(DB.getConnection(),"select * from employee where username = ?",username),
                Employee.class
        );
        return employee;
    }


    /**
     *  分页查询
     * @param where 条件参数
     * @param page 当前页码
     * @param size 每一页显示的记录数量
     * @return resultToList
     * @throws SQLException 异常
     * @throws CastResultException 异常
     */
    @Override
    public ArrayList<Employee> list(Map<String, Object> where, int page, int size) throws SQLException, CastResultException {
        String sql = "select * from employee where 1=1 ";
        ArrayList<Object> values = new ArrayList<>();
        if (where.containsKey("name")) {
            sql += " and name like ?"; // 我们这里使用sql模糊查询
            values.add("%" + where.get("name") + "%"); // 占位符
        }
        if (where.containsKey("status")) {
            sql += " and status like ?";
            values.add(where.get("status"));
        }
        sql += " order by create_time limit ?,?";  // 分页
        values.add((page - 1) * size);
        values.add(size);

        return DB.resultToList(
                DB.query(
                        DB.getConnection(),
                        sql,
                        values
                ),Employee.class
        );
    }

    /**
     *  根据条件统计记录
     * @param where
     * @return
     * @throws SQLException
     */
    @Override
    public int getTotal(Map<String, Object> where) throws SQLException {
        String sql = "select count(*) as c from employee where 1=1 ";
        ArrayList<Object> values = new ArrayList<>();
        if (where.containsKey("name")) {
            sql += " and name like ?"; // 我们这里使用sql模糊查询
            values.add("%" + where.get("name") + "%"); // 占位符
        }
        if (where.containsKey("status")) {
            sql += " and status like ?";
            values.add(where.get("status"));
        }
        ResultSet rs = DB.query(
                DB.getConnection(),
                sql,
                values
        );
        rs.next();
        return rs.getInt("c");
    }



    @Override
    public boolean delete(long id) throws SQLException {
        return false;
    }

    @Override
    public boolean insert(Employee employee) throws SQLException {
        return DB.execute(
                DB.getConnection(),
                "INSERT INTO employee(`name`,username,`password`,phone,sex,id_number,`status`," +
                        "create_time,update_time,create_user,update_user) VALUES(?,?,?,?,?,?,?,?,?,?,?)",
                employee.getName(), employee.getUsername(), employee.getPassword(), employee.getPhone(), employee.getSex(),
                employee.getId_number(), employee.getStatus(), employee.getCreate_time(), employee.getUpdate_time(),
                employee.getCreate_user(), employee.getUpdate_user()
        );
    }

    @Override
    public boolean update(Employee employee) throws SQLException {
        return DB.execute(
                DB.getConnection(),
                "update Employee set name = ?,username = ?,password = ?,phone = ?,sex = ?,id_number = ?," +
                        "status = ?,create_time = ?,update_time = ?,create_user = ?,update_user = ? where id = ?",
                employee.getName(),employee.getUsername(),employee.getPassword(),employee.getPhone(),employee.getSex(),
                employee.getId_number(),employee.getStatus(),employee.getCreate_time(),employee.getUpdate_time(),
                employee.getCreate_user(),employee.getUpdate_user(),employee.getId()
        );
    }
}
