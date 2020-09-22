package com.mumu.JDBCPool;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Logger;


public class MyPool implements DataSource {
    private int INIT_COUNT = 5;
    private int MAX_COUNT = 10;
    private int CUR_COUNT = 0;

    private LinkedList <Connection> list = new LinkedList<Connection>();
    ArrayList arrayList = null;

    public MyPool(){
        for (int i = 0; i < INIT_COUNT; i++){
            Connection connection = createConnection();
            list.add(connection);
        }
    }

    public Connection createConnection() {
        Connection procyConnection = null;
        try {
            Connection connection = JDBCUtils.getConnection();
            procyConnection = (Connection) Proxy.newProxyInstance(MyPool.class.getClassLoader(), new Class[]{Connection.class}, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    String methodName = method.getName();
                    if (methodName.equalsIgnoreCase("close")){
                        list.addLast((Connection) proxy);
                    }else {
                        return method.invoke(connection,args);
                    }
                    return null;
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        CUR_COUNT++;
        return procyConnection;
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (list.size() > 0){
            return list.removeFirst();
        }
        if (CUR_COUNT <= MAX_COUNT){
            return createConnection();
        }else {
            throw new RuntimeException("达到最大连接数");
        }
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
