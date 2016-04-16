package services;

import models.RawDataModel;
import utils.DB;
import utils.OracleDAO;

import java.util.ArrayList;

/**
 * Created by Araja Jyothi Babu on 15-Apr-16.
 */
public class RawDataService {

    OracleDAO dao;

    public RawDataService(OracleDAO dao) {
        this.dao = dao;
    }

    public ArrayList<RawDataModel> getRawData() throws Exception{
        ArrayList<RawDataModel> rawDataModels = dao.getRawData();
        return rawDataModels;
    }

    public RawDataModel getRawData(int id) throws Exception{
        RawDataModel rawData = dao.getRawData(id);
        return rawData;
    }

    public boolean insertRawData(RawDataModel rawDataModel) throws Exception {
        boolean isInserted = dao.insertRawData(rawDataModel);
        return isInserted;
    }

    public boolean insertRawData(ArrayList<RawDataModel> rawDataModels) throws Exception {
        boolean isInserted = dao.insertRawData(rawDataModels);
        return isInserted;
    }

}
