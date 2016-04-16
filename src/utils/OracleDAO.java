package utils;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * Created by Araja Jyothi Babu on 15-Apr-16.
 */
public class OracleDAO {

    private DB db;
    private Connection connection;

    public OracleDAO(DB db, Connection connection) {
        this.db = db;
        this.connection = connection;
    }

    public OracleDAO() {
    }

    public static String getDiagnosis()

}
