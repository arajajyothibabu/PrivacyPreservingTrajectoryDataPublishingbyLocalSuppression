package utils;

import models.SensitiveDataModel;
import models.TrajectoryDataModel;

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

    public static SensitiveDataModel getSensitiveData(int id) throws Exception {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM sensitivedata WHERE id = '" + id + "'");
        SensitiveDataModel sensitiveData = new SensitiveDataModel(0, "Healthy"); //FIXME: handling not found in db
        if(resultSet.next()){
            sensitiveData = Utils.makeSensitiveDataModel(resultSet);
        }
        return sensitiveData;
    }

    public static ArrayList<TrajectoryDataModel> getTrajectoryData(int id) throws Exception {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM trajectorydata WHERE id = '" + id + "'");
        ArrayList<TrajectoryDataModel> trajectoryDataModels = new ArrayList();
        while(resultSet.next()){
            trajectoryDataModels.add(Utils.makeTrajectoryDataModel(resultSet));
        }
        return trajectoryDataModels;
    }

    

}
