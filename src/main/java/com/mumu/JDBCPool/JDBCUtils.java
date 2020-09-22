package com.mumu.JDBCPool;
import java.sql.*;
public class JDBCUtils {
    private static final String DRIVER_CLASS = "com.sql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost/mumu?useServerPrepStmts=false";
    private static final String USER_NAME = "root";
    private static final String PASS_WORD = "root";
    //静态代码块注册驱动，只注册一次
    static {
        try {
            Class.forName(DRIVER_CLASS);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //得到连接
    public static Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(URL,USER_NAME,PASS_WORD);
        return connection;
    }

    public static void close(Connection connection, Statement statement){
        close(connection,statement,null);
    }

    public static void close(Connection connection, Statement statement ,ResultSet resultSet){
        if (connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (statement != null){
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (resultSet != null){
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
