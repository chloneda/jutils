package com.chloneda.utils.jdbc;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by chloneda
 * Description:
 */
public class ResultSetUtilsTest {
    private String url="jdbc:mysql://127.0.0.1:3306/common?characterEncoding=UTF8&amp;useSSL=false";
    private String username="root";
    private String password="123456";
    Connection connection;
    Statement stat;
    ResultSet resultSet;
    QueryRunner runner;

    @Before
    public void init() throws SQLException {
        connection=DriverManager.getConnection(url,username,password);
        stat=connection.createStatement();

        //runner = new QueryRunner();
    }

    @Test
    public void testConvertMap() throws SQLException {
        resultSet=stat.executeQuery("select * from new_table");
        Map<String, List<String>> dataMap= ResultSetUtils.toMap(resultSet);
        System.out.println(dataMap);
    }

    @Test
    public void testConvertList() throws SQLException {
        resultSet=stat.executeQuery("select * from new_table");
        List<Map<String, Object>> dataMap= ResultSetUtils.toList(resultSet);
        System.out.println(dataMap);
    }

    @Test
    public void testConvertSet() throws SQLException {
        resultSet=stat.executeQuery("select * from new_table");
        Set<String> dataMap= ResultSetUtils.convertSet(resultSet);
        System.out.println(dataMap);
    }

    @After
    public void destory() throws SQLException {
        DbUtils.closeQuietly(connection,stat,resultSet);
    }
}
