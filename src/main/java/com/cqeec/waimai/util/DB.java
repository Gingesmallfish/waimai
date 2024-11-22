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
 *
 **/
public class DB {
    private static Connection connection = null;

    public DB() {
    }

    public static void  startTransaction() throws SQLException {
        connection.setAutoCommit(false);
    }
    public static void commitTransaction() throws SQLException {
        connection.commit();
    }
    public static void rollbackTransaction() throws SQLException {
        connection.rollback();
    }
    public static Connection getConnection() throws SQLException {
        // 如果connection不为空且未关闭，则直接返回现有连接
        if (connection != null && !connection.isClosed()) {
            return connection;
        }
        // 从资源文件中获取连接信息建立新的数据库连接
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");//加载驱动
            connection = DriverManager.getConnection(String.format("jdbc:mysql://%s:%s/%s ?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai",
                            SystemConfig.getPropertiesByName("db.host"),
                            SystemConfig.getPropertiesByName("db.port"),
                            SystemConfig.getPropertiesByName("db.name")),
                    SystemConfig.getPropertiesByName("db.username"),
                    SystemConfig.getPropertiesByName("db.password")
            );
        }  catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

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
            statement.setObject(i++, iterator.next());
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

    public static boolean execute(Connection connection, String sql) throws SQLException {
        Statement statement = null;
        statement = connection.createStatement();
        return statement.execute(sql);
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
            statement.setObject(i++, iterator.next());
        }
        statement.executeUpdate();
        return true;
    }

    /**
     * 将结果集转换为指定类型的ArrayList
     * 此方法通过反射机制将数据库查询结果的每一行转换为指定的自定义对象类型
     * @param resultSet 数据库查询结果集
     * @param clazz 目标对象类型，用于通过反射实例化对象并设置属性值
     * @return 包含转换后对象的ArrayList
     * @param <T> 泛型类型，表示目标对象类型
     */
    public static <T> ArrayList<T> resultToList(ResultSet resultSet, Class<T> clazz) throws CastResultException {
        ResultSetMetaData metaData = null;
        ArrayList<T> list = new ArrayList();
        try {
            // 获取结果集的元数据，用于后续的列名和数据类型访问
            metaData = resultSet.getMetaData();
            // 获取结果集中的列数，即后续需要处理的数据列数量
            int columnCount = metaData.getColumnCount();
            // 遍历结果集，并将每行数据转换为目标类型对象
            while (resultSet.next()) {
                // 通过反射机制实例化目标类型对象
                T obj = clazz.getConstructor().newInstance();
                for (int i = 1; i <= columnCount; ++i) {
                    // 获取当前列的列名，用于匹配目标对象的字段
                    String columnName = metaData.getColumnName(i);
                    // 获取当前列的值
                    Object columnValue = resultSet.getObject(i);
                    // 类型转换
                    if (columnValue instanceof BigInteger) {
                        columnValue = ((BigInteger) columnValue).longValue();
                    }
                    if (columnValue instanceof BigDecimal) {
                        columnValue = ((BigDecimal) columnValue).doubleValue();
                    }
                    if (columnValue instanceof LocalDateTime) {
                        columnValue = Timestamp.valueOf((LocalDateTime) columnValue);
                    }
                    // 通过反射获取目标对象的字段，并设置其值
                    Field field = clazz.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(obj, columnValue);
                }
                // 将转换后的对象添加到列表中
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
