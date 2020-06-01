package com.example.viewpager_test;

import android.util.Log;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtils {

    // MySQL 8.0 以上版本 - JDBC 驱动名及数据库 URL
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String conn_ = "jdbc:mysql://119.27.176.211:10005/together?serverTimezone=Asia/Shanghai&&useSSL=false&&allowPublicKeyRetrieval=true";
    //    private static final String DB_URL = "jdbc:mysql://193.112.231.132:10112/together?serverTimezone=Asia/Shanghai&&useSSL=false&&allowPublicKeyRetrieval=true";
//    private static final String DB_URL = "jdbc:mysql://address=(protocol=tcp)(host=2409:8a62:6e1c:2180:e090:182c:de1a:c37f)(port=3306)/together?serverTimezone=Asia/Shanghai&&useSSL=false&&allowPublicKeyRetrieval=true";
//    private static String DB_URL = "jdbc:mysql://address=(protocol=tcp)(host=409:8a34:a33:f9e0:e090:182c:de1a:c37f)(port=3306)/together?serverTimezone=Asia/Shanghai&&useSSL=false&&allowPublicKeyRetrieval=true";
    private static String DB_URL="";
    // 数据库的用户名与密码，需要根据自己的设置
    private static final String USER = "customer";
    private static final String PASS = "9876541233";

    private static void getaddr() {

                Connection conn = null;
                Statement stmt = null;
                try {
                    // 注册 JDBC 驱动
                    Class.forName(JDBC_DRIVER);

                    // 打开链接
                    Log.d("TAGG", "连接数据库...");
                    conn = DriverManager.getConnection(conn_, USER, PASS);

                    stmt = conn.createStatement();

                    ResultSet rs = stmt.executeQuery("SELECT address_ipv6 FROM addr");

                    rs.beforeFirst();

                    // 展开结果集数据库
                    if (rs.next()) {
                        DB_URL = "jdbc:mysql://address=(protocol=tcp)(host="+rs.getNString("address_ipv6")+")(port=3306)/together?serverTimezone=Asia/Shanghai&&useSSL=false&&allowPublicKeyRetrieval=true";
                        Log.e("静态代码块：",rs.getNString("address_ipv6"));
                    }
                    // 完成后关闭
                    rs.close();
                    stmt.close();
                    conn.close();
                } catch (Exception se) {
                    // 处理 JDBC 错误
                    se.printStackTrace();
                }// 处理 Class.forName 错误
                finally {
                    // 关闭资源
                    try {
                        if (stmt != null) stmt.close();
                    } catch (SQLException ignored) {
                    }// 什么都不做
                    try {
                        if (conn != null) conn.close();
                    } catch (SQLException se) {
                        se.printStackTrace();
                    }
                }

    }

    @SuppressWarnings("all")
    public static String[][] select_DB(String sql, String... strings) {
        String[][] reStrs = null;
        int x = 0;
        Connection conn = null;
        Statement stmt = null;
        if(DB_URL=="")
            getaddr();
        try {
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            Log.d("TAGG", "连接数据库...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();
            if (sql.equals(""))
                sql = "SELECT * FROM members";

            ResultSet rs = stmt.executeQuery(sql);

            rs.last();
            reStrs = new String[rs.getRow()][strings.length];
            rs.beforeFirst();

            // 展开结果集数据库
            while (rs.next()) {
                // 通过字段检索
                for (int i = 0; i < strings.length; i++)
                    reStrs[x][i] = rs.getString(strings[i]);
                x++;

            }
            // 完成后关闭
            Log.d("TAGG", "成功获取数据...");
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        }// 处理 Class.forName 错误
        finally {
            // 关闭资源
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException ignored) {
            }// 什么都不做
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return reStrs;
    }

    static InputStream[] selectBLOB(String sql, String string) {
        InputStream[] reis = null;
        int x = 0;
        Connection conn = null;
        Statement stmt = null;
        if(DB_URL=="")
            getaddr();
        try {
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            Log.d("TAGG", "连接数据库...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(sql);

            rs.last();
            reis = new InputStream[rs.getRow()];
            rs.beforeFirst();

            // 展开结果集数据库
            while (rs.next()) {
                // 通过字段检索
                reis[x] = rs.getBinaryStream(string);
                x++;
            }
            // 完成后关闭
            Log.d("TAGG", "成功获取数据...");
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        }// 处理 Class.forName 错误
        finally {
            // 关闭资源
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException ignored) {
            }// 什么都不做
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return reis;
    }

    public static int _DB(String sql) {
        int reint = 0;
        Connection conn = null;
        Statement stmt = null;
        if(DB_URL=="")
            getaddr();
        try {
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            Log.d("TAGG", "连接数据库...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();
            if (sql.equals(""))
                sql = "SELECT * FROM members WHERE S_ID>1000000000";
            reint = stmt.executeUpdate(sql);

            Log.e("TAGG", "成功操作数据行数：" + reint);
            stmt.close();
            conn.close();
        } catch (Exception se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        }// 处理 Class.forName 错误
        finally {
            // 关闭资源
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException ignored) {
            }// 什么都不做
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return reint;
    }
}
