package services;

import models.SensitiveDataModel;
import utils.DB;
import utils.OracleDAO;

/**
 * Created by Araja Jyothi Babu on 15-Apr-16.
 */
public class SensitiveDataService {

    OracleDAO dao;

    public SensitiveDataService(OracleDAO dao) {
        this.dao = dao;
    }

    public SensitiveDataModel getSensitiveData(int id) throws Exception {
        SensitiveDataModel sensitiveDataModel = dao.getSensitiveData(id);
        return sensitiveDataModel;
    }

}
