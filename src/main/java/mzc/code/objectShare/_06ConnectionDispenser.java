package mzc.code.objectShare;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * page:37
 * 本例演示了另一种更规范的方法保持线程封闭性，即通过 ThreadLocal 来将变量封闭在线程中
 */
public class _06ConnectionDispenser {

    static String DB_URL = "jdbc:mysql://localhost/mydatabase";

    private static ThreadLocal<Connection> connectionHolder = ThreadLocal.withInitial(() -> {
        try {
            return DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    });

    public static Connection getConnection() {
        return connectionHolder.get();
    }

}
