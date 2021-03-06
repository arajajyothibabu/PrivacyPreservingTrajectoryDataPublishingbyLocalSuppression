package utils;

import models.RawDataModel;
import models.SensitiveDataModel;
import models.TrajectoryDataModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Araja Jyothi Babu on 15-Apr-16.
 */
public class OracleDAO {

    private DB db;
    private Connection connection;

    public OracleDAO(DB db) throws Exception{
        this.db = db;
        this.connection = db.openConnection();
    }

    public OracleDAO() {
    }

    public SensitiveDataModel getSensitiveData(int id) throws Exception {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM sensitivedata WHERE id = '" + id + "'");
        SensitiveDataModel sensitiveData = new SensitiveDataModel(0, "Healthy"); //FIXME: handling not found in db
        if(resultSet.next()){
            sensitiveData = Utils.makeSensitiveDataModel(resultSet);
        }
        return sensitiveData;
    }

    public ArrayList<SensitiveDataModel> getSensitiveData() throws Exception {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM sensitivedata");
        ArrayList<SensitiveDataModel> sensitiveDataModels = new ArrayList();
        while(resultSet.next()){
            sensitiveDataModels.add(Utils.makeSensitiveDataModel(resultSet));
        }
        return sensitiveDataModels;
    }

    public ArrayList<TrajectoryDataModel> getTrajectoryData(int id) throws Exception {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM trajectorydata WHERE id = '" + id + "' ORDER BY time");
        ArrayList<TrajectoryDataModel> trajectoryDataModels = new ArrayList();
        while(resultSet.next()){
            trajectoryDataModels.add(Utils.makeTrajectoryDataModel(resultSet));
        }
        return trajectoryDataModels;
    }

    public ArrayList<String> getAllUniqueDoublets() throws Exception {
        ArrayList<String> uniqueDoublets = new ArrayList();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT DISTINCT location, time FROM trajectorydata ORDER BY location");
        while(resultSet.next()){
            uniqueDoublets.add(Utils.makeDoublet(resultSet));
        }
        return uniqueDoublets;
    }

    public boolean insertRawData(RawDataModel rawDataModel) throws Exception{
        PreparedStatement statement = connection.prepareStatement("INSERT INTO rawdatatable VALUES (?,?,?)");
        statement.setInt(1, rawDataModel.getId());
        statement.setString(2, rawDataModel.getPath());
        statement.setString(3, rawDataModel.getDiagnosis());
        boolean isInserted = statement.execute();
        return isInserted;
    }

    public boolean insertRawData(ArrayList<RawDataModel> rawDataModels) throws Exception{
        int insertedCount = 0;
        for(RawDataModel rawDataModel : rawDataModels){
            if(insertRawData(rawDataModel))
                insertedCount++;
        }
        return insertedCount == rawDataModels.size();
    }

    public ArrayList<RawDataModel> getRawData() throws Exception {
        ArrayList<RawDataModel> rawDataModels = new ArrayList();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM rawdatatable ORDER BY id");
        while(resultSet.next()){
            rawDataModels.add(Utils.makeRawDataModel(resultSet));
        }
        return rawDataModels;
    }

    private String createQuery(String selectClause, int length) {
        String query = selectClause + " IN (";
        StringBuilder queryBuilder = new StringBuilder(query);
        for( int i = 0; i< length; i++){
            queryBuilder.append(" ?");
            if(i != length -1) queryBuilder.append(",");
        }
        if(length == 0)
            queryBuilder.append("0");
        queryBuilder.append(")");
        return queryBuilder.toString();
    }

    public ArrayList<RawDataModel> getRawData(ArrayList<String> S) throws Exception {
        ArrayList<RawDataModel> rawDataModels = new ArrayList();
        String selectClause = "SELECT * FROM rawdatatable WHERE diagnosis";
        int sizeOfS = S.size();
        PreparedStatement statement = connection.prepareStatement(createQuery(selectClause, sizeOfS));
        for(int i = 0; i < sizeOfS; i++){
            statement.setString(i+1, S.get(i));
        }
        ResultSet resultSet = statement.executeQuery();
        while(resultSet.next()){
            rawDataModels.add(Utils.makeRawDataModel(resultSet));
        }
        return rawDataModels;
    }

    public RawDataModel getRawData(int id) throws Exception {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM rawdatatable");
        RawDataModel rawData = new RawDataModel();
        if(resultSet.next()){
            rawData = Utils.makeRawDataModel(resultSet);
        }
        return rawData;
    }

    public boolean emptyRawData() throws Exception {
        Statement statement = connection.createStatement();
        boolean isTruncated = statement.execute("TRUNCATE TABLE rawdatatable");
        return isTruncated;
    }

}
