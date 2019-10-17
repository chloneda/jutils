package com.chloneda.jutils.jdbc.handlers;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * @Created by chloneda
 * @Description:
 */
public interface JDBCCallBack<T> {

    T doJDBCConnection(Statement statement)throws SQLException;

}
