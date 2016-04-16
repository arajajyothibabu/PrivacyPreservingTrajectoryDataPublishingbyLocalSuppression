package utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Araja Jyothi Babu on 15-Apr-16.
 */
public class OracleDAO {

    private static DB db;
    private static Connection connection;

    public OracleDAO(DB db, Connection connection) {
        this.db = db;
        this.connection = connection;
    }

    public OracleDAO() {
    }

    public static String getDiagnosis(int id) throws Exception {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM sensitivedata WHERE id = '" + id + "'");
        if(resultSet.next()){

        }
    }

}
