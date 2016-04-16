package services;

import models.TrajectoryDataModel;
import utils.OracleDAO;

import java.util.ArrayList;

/**
 * Created by Araja Jyothi Babu on 15-Apr-16.
 */
public class TrajectoryDataService {

    OracleDAO dao;

    public TrajectoryDataService(OracleDAO dao) {
        this.dao = dao;
    }

    public ArrayList<String> getAllUniqueDoublets() throws Exception {
        ArrayList<String> doublets = dao.getAllUniqueDoublets();
        return doublets;
    }

    public ArrayList<TrajectoryDataModel> getTrajectoryData(int id) throws Exception {
        ArrayList<TrajectoryDataModel> trajectoryDataModels = dao.getTrajectoryData(id);
        return trajectoryDataModels;
    }

}
