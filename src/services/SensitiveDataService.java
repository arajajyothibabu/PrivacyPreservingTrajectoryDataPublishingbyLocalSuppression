package services;

import models.SensitiveDataModel;
import utils.DB;
import utils.OracleDAO;

/**
 * Created by Araja Jyothi Babu on 15-Apr-16.
 */
public class SensitiveDataService {
    //TODO:

    DB db;
    OracleDAO dao;

    public SensitiveDataService(DB db) throws Exception{
        this.db = db;
        this.dao = new OracleDAO(db);
    }

    public SensitiveDataModel getSensitiveData(int id) throws Exception {
        SensitiveDataModel sensitiveDataModel = dao.getSensitiveData(id);
        return sensitiveDataModel;
    }

}
