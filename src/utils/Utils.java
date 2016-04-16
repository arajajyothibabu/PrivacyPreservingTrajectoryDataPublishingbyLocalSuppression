package utils;

import models.RawDataModel;
import models.SensitiveDataModel;
import models.TrajectoryDataModel;

import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by Araja Jyothi Babu on 15-Apr-16.
 */
public class Utils {
    //global functions for use all over project

    public static SensitiveDataModel makeSensitiveDataModel(ResultSet rs) throws Exception{
        return new SensitiveDataModel(rs.getString(1));
    }

    public static TrajectoryDataModel makeTrajectoryDataModel(ResultSet rs) throws Exception {
        return new TrajectoryDataModel(rs.getInt(1), rs.getString(2), rs.getString(3));
    }

    public static RawDataModel makeRawDataModel(ResultSet rs) throws Exception {
        return new RawDataModel(rs.getInt(1), rs.getString(2), rs.getString(3));
    }

    public static String makeDoublet(TrajectoryDataModel trajectoryDataModel) throws Exception {
        return trajectoryDataModel.getLocation() + "" + trajectoryDataModel.getTime();
    }

    public static String makeSingleString(ArrayList<String> doublets) throws Exception {
        StringBuilder path = new StringBuilder("");
        for(String doublet : doublets){
            path.append(doublet);
        }
        return path.toString();
    }

}
