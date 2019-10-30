package com.chloneda.jutils.jdbc.handlers;

import javafx.util.Callback;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * @Created by chloneda
 * @Description: {@link Callback},
 * {@link java.util.concurrent.Callable}
 */
public interface JDBCCallBack<T> extends Callback<T, Statement> {

    T doJDBCConnection(Statement statement) throws SQLException;

}
