//package com.chloneda.utils.jdbc;
//
//import org.apache.commons.dbutils.DbUtils;
//import org.apache.commons.dbutils.ResultSetHandler;
//
//import java.sql.*;
//
///**
// * @Created by chloneda
// * @Description:
// */
//public class QueryUtils {
//    public <T> T query(Connection conn, String sql, ResultSetHandler<T> rsh) throws SQLException {
//        return this.query(conn, false, sql, rsh, (Object[])null);
//    }
//
//    private <T> T query(Connection conn, boolean closeConn, String sql, ResultSetHandler<T> rsh, Object... params) throws SQLException {
//        if (conn == null) {
//            throw new SQLException("Null connection");
//        } else if (sql == null) {
//            if (closeConn) {
//                this.close(conn);
//            }
//
//            throw new SQLException("Null SQL statement");
//        } else if (rsh == null) {
//            if (closeConn) {
//                this.close(conn);
//            }
//
//            throw new SQLException("Null ResultSetHandler");
//        } else {
//            PreparedStatement stmt = null;
//            ResultSet rs = null;
//            Object result = null;
//
//            try {
//                stmt = this.prepareStatement(conn, sql);
//                this.fillStatement(stmt, params);
//                rs = stmt.executeQuery();
//                result = rsh.handle(rs);
//            } catch (SQLException var33) {
//            } finally {
//                try {
//                    this.close(rs);
//                } finally {
//                    this.close(stmt);
//                    if (closeConn) {
//                        this.close(conn);
//                    }
//
//                }
//            }
//
//            return result;
//        }
//    }
//
//    protected PreparedStatement prepareStatement(Connection conn, String sql) throws SQLException {
//        PreparedStatement ps = conn.prepareStatement(sql);
//
//        try {
//            this.configureStatement(ps);
//            return ps;
//        } catch (SQLException var5) {
//            ps.close();
//            throw var5;
//        }
//    }
//
//    protected void close(Connection conn) throws SQLException {
//        DbUtils.close(conn);
//    }
//
//    protected void close(Statement stmt) throws SQLException {
//        DbUtils.close(stmt);
//    }
//
//    protected void close(ResultSet rs) throws SQLException {
//        DbUtils.close(rs);
//    }
//}
