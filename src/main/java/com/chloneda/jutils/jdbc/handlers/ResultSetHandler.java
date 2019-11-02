package com.chloneda.jutils.jdbc.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Created by chloneda
 * @Description:
 */
public interface ResultSetHandler<T> {

    T handle(ResultSet rs) throws SQLException;

}
