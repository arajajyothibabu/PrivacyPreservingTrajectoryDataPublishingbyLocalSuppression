package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Created by Araja Jyothi Babu on 15-Apr-16.
 */
public class DB {

    private static String DBURL = "jdbc:oracle:thin:@localhost:1521:XE";
    private static String DBUSER = "system";
    private static String DBPASS = "tiger";
    private static Connection connection;

    public DB() {
    }

    public static Connection openConnection() throws Exception {

        Class.forName("oracle.jdbc.driver.OracleDriver");

        connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS );

        return connection;

    }

    public static void closeConnection() throws Exception {
        connection.close();
    }

    public static Boolean commitDB() throws Exception {
        Statement commitStatement = connection.createStatement();
        return commitStatement.execute("COMMIT");
    }

}
