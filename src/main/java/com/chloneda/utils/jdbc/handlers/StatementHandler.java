package com.chloneda.utils.jdbc.handlers;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by chloneda
 * Description:
 */
public interface StatementHandler<T> {

    T handle(Statement statement) throws SQLException;

}
