package com.cqeec.waimai.util;

import com.cqeec.waimai.exception.CastResultException;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *  数据工具类
 **/
public class DB {
    private static Connection connection = null;

    public DB() {
    }

    // 开启事务
    public static void  startTransaction() throws SQLException {
        connection.setAutoCommit(false);
    }
    // 提交事务
    public static void commitTransaction() throws SQLException {
        connection.commit();
    }
    // 事务回滚
    public static void rollbackTransaction() throws SQLException {
        connection.rollback();
    }
    public static Connection getConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            return connection;
        }
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");//加载驱动
            connection = DriverManager.getConnection(
                    "jdbc:mysql:///reggie?useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8", // 这个东八区，的时间，也表示北京时间
                    "root",
                    "123456"
            );
        }  catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }
    /* 执行查询 */
    public static ResultSet query(Connection connection, String sql) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        return resultSet;
    }

    public static ResultSet query(Connection connection, String sql, ArrayList<Object> values) throws SQLException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        statement = connection.prepareStatement(sql);
        Iterator<Object> iterator = values.iterator();
        int i = 1;
        while(iterator.hasNext()){
            statement.setObject(i, iterator.next());
        }
        resultSet = statement.executeQuery();
        return resultSet;
    }

    public static ResultSet query(Connection connection, String sql, Object... params) throws SQLException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        statement = connection.prepareStatement(sql);
        for(int i = 0; i < params.length; ++i) {
            statement.setObject(i + 1, params[i]);
        }
        resultSet = statement.executeQuery();
        return resultSet;
    }

    /* 执行增加，删除，修改 */
    public static boolean execute(Connection connection, String sql) throws SQLException {
        Statement statement = null;
        statement = connection.createStatement();
        statement.executeUpdate(sql);
        return true;
    }

    public static boolean execute(Connection connection, String sql, Object... params) throws SQLException {
        PreparedStatement statement = null;
        statement = connection.prepareStatement(sql);
        for(int i = 0; i < params.length; ++i) {
            statement.setObject(i + 1, params[i]);
        }
        statement.executeUpdate();
        return true;
    }

    public static boolean execute(Connection connection, String sql, ArrayList<Object> values) throws SQLException {
        PreparedStatement statement = null;
        statement = connection.prepareStatement(sql);
        Iterator<Object> iterator = values.iterator();
        int i = 1;
        while(iterator.hasNext()){
            statement.setObject(i, iterator.next());
        }
        statement.executeUpdate();
        return true;
    }

    /* 将查询结果集转换成ArrayList对象 */
    public static <T> ArrayList<T> resultToList(ResultSet resultSet, Class<T> clazz) throws CastResultException {
        ResultSetMetaData metaData = null;
        ArrayList<T> list = new ArrayList();
        try {
            metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (resultSet.next()) {
                T obj = clazz.getConstructor().newInstance();
                for (int i = 1; i <= columnCount; ++i) {
                    String columnName = metaData.getColumnName(i);
                    Object columnValue = resultSet.getObject(i);
                    if (columnValue instanceof BigInteger) {
                        columnValue = ((BigInteger) columnValue).longValue();
                    }

                    if (columnValue instanceof BigDecimal) {
                        columnValue = ((BigDecimal) columnValue).doubleValue();
                    }
                    if (columnValue instanceof LocalDateTime) {
                        columnValue = Timestamp.valueOf((LocalDateTime) columnValue);
                    }
                    Field field = clazz.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(obj, columnValue);
                }
                list.add(obj);
            }
        }
        catch (Exception e){
            throw new CastResultException();
        }
        return list;
    }

    public static <T> T resultToObj(ResultSet resultSet, Class<T> clazz) throws CastResultException {
        List<T> list = resultToList(resultSet, clazz);
        return list.size() > 0 ? list.get(0) : null;
    }
}
