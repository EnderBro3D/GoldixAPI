package mc.enderbro3d.goldixapi.data;


import mc.enderbro3d.goldixapi.data.values.Value;
import org.intellij.lang.annotations.Language;

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
    public static int execute(boolean async, @Language(value = "MySQL") String sql, Object... params) {
        try {
            PreparedStatement statement = createStatement(sql, params);
            return async ? asyncTask(statement::executeUpdate).get() : statement.executeUpdate();
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
    public static ResultSet executeQuery(boolean async, @Language(value = "MySQL") String sql, Object... params) {
        try {
            PreparedStatement statement = createStatement(sql, params);
            return async ? asyncTask(statement::executeQuery).get() : statement.executeQuery();
        } catch (Exception e) {
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
    public static PreparedStatement createStatement(@Language(value = "MySQL") String sql, Object... params) {
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
