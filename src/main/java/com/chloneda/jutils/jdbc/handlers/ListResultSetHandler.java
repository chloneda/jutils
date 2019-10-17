package com.chloneda.jutils.jdbc.handlers;

import com.chloneda.jutils.jdbc.ResultSetUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @Created by chloneda
 * @Description:
 */
public class ListResultSetHandler implements ResultSetHandler<List<Map<String,Object>>> {

    @Override
    public List<Map<String,Object>> handle(ResultSet rs) throws SQLException {
        return ResultSetUtils.toList(rs);
    }

}
