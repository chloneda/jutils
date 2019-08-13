package com.chloneda.jutils.jdbc;

import java.io.*;
import java.sql.*;
import java.util.*;

/**
 * Created by chloneda
 * Description:
 */
public class ResultSetUtils {

    public static byte[] toByteArray(Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }

    /**
     * 数组转对象
     */
    public static Object toObject(byte[] bytes) {
        Object obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();
            ois.close();
            bis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    /**
     * List转换数组
     */
    @SuppressWarnings("unchecked")
    public static String[] toStringArray(Collection coll) {
        return (String[]) coll.toArray(new String[coll.size()]);
    }

    /**
     * Set转换为List
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static List toList(Set set) {
        return new ArrayList(Arrays.asList(set));
    }

    /**
     * ResultSet转换为List
     */
    public static List<Map<String, Object>> toList(ResultSet rs) throws SQLException {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResultSetMetaData md = rs.getMetaData();
        int columnCount = md.getColumnCount(); //Map rowData;

        while (rs.next()) {
            Map<String, Object> rowData = new HashMap<String, Object>();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = md.getColumnLabel(i).toUpperCase();
                rowData.put(columnName, rs.getObject(i));
            }
            list.add(rowData);
        }
        return list;
    }

    /**
     * ResultSet转换为Map
     */
    public static Map<String, List<String>> toMap(ResultSet rs) throws SQLException {
        Map<String, List<String>> map = new LinkedHashMap<String, List<String>>();
        int rowLengh = rs.getMetaData().getColumnCount();
        String lastVal = null;
        boolean isSame = true;
        boolean first = true;
        List<String> tableNames = new LinkedList<String>();
        while (rs.next()) {
            for (int i = 1; i <= rowLengh; i++) {
                String nowVal = rs.getString(i);
                if (i == 1) {
                    if (lastVal != null && !lastVal.equals(nowVal)) {
                        isSame = false;
                    } else if (first) {
                        lastVal = nowVal;
                        first = false;
                    }
                } else {
                    tableNames.add(nowVal);
                }
                if (!isSame) {
                    map.put(lastVal, tableNames);
                    isSame = true;
                    tableNames = new LinkedList<String>();
                    lastVal = nowVal;
                }
            }
        }
        map.put(lastVal, tableNames);
        return map;
    }

    public static String[] toArray(String datas, String split) {
        return datas.split(split);
    }

    public static Set<String> convertSet(ResultSet rs) throws SQLException {
        Set<String> tables = new LinkedHashSet<String>();
        ResultSetMetaData md = rs.getMetaData();
        int columnCount = md.getColumnCount(); //Map rowData;
        while (rs.next()) { //rowData = new HashMap(columnCount);
            for (int i = 1; i <= columnCount; i++) {
                tables.add(rs.getObject(i).toString());
            }
        }
        return tables;
    }


}
