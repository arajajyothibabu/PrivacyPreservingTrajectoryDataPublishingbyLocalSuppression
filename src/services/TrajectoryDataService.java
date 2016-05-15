package services;

import models.TrajectoryDataModel;
import utils.OracleDAO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import static utils.Utils.numberFromString;

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
        Collections.sort(doublets, new Comparator<String>() { //sorting based on time rather than location
            @Override
            public int compare(String s1, String s2) {
                return numberFromString(s1)-numberFromString(s2);
            }
        });
        return doublets;
    }

    public ArrayList<TrajectoryDataModel> getTrajectoryData(int id) throws Exception {
        ArrayList<TrajectoryDataModel> trajectoryDataModels = dao.getTrajectoryData(id);
        return trajectoryDataModels;
    }

}
