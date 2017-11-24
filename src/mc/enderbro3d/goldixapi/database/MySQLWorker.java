package mc.enderbro3d.goldixapi.database;


import mc.enderbro3d.goldixapi.data.Value;

import java.sql.*;
import java.util.concurrent.*;

public class MySQLWorker {

    private static Connection connection;

    public static void connect(String user, String password, String database, String host, int port) {
        try {
            disconnect();
            connection = DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/"+database+"?user="+user+"&password="+password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void disconnect() {
        if(connection == null) return;

        try {
            connection.close();
        } catch (SQLException ignored) { }
    }

    /**
     * Выполняет запрос
     * @param sql - SQL запрос
     * @param params - Параметры
     * @return - Номер изменённой строки
     */
    public static int execute(String sql, Object... params) {
        try {
            PreparedStatement statement = createStatement(sql, params);
            return asyncTask(statement::executeUpdate).get();
        } catch(Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Выполняет запрос
     * @param sql - SQL запрос
     * @param params - Параметры
     * @return ResultSet
     */

    public static ResultSet executeQuery(String sql, Object... params) {
        try {
            PreparedStatement statement = createStatement(sql, params);
            return asyncTask(statement::executeQuery).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Создаёт Statement
     * @param sql - SQL запрос
     * @param params - Параметры
     * @return - Возвращает Statement
     */
    public static PreparedStatement createStatement(String sql, Object... params) {
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            for(int i = 0;i < params.length;i++) {
                statement.setString(i + 1, params[i].toString());
            }
            return statement;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Копия rs.next, но без SQLException
     * @param rs - ResultSet
     * @return Возвращает, может ли идти дальше
     */
    public static boolean next(ResultSet rs) {
        try {
            return rs.next();
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Копия rs.getObject(String), но без SQLException9
     * @param rs - ResultSet
     * @param key - Ключ
     * @return Возвразает значение
     */
    public static Value get(ResultSet rs, String key) {
        try {
            return new Value(rs.getObject(key));
        } catch(SQLException e) {
            e.printStackTrace();
            return new Value(null);
        }
    }

    private static <T> Future<T> asyncTask(Callable<T> callable) {
        return createQueryExecutor().submit(callable);
    }


    private static ExecutorService createQueryExecutor() {
        return Executors.newSingleThreadExecutor();
    }
}
