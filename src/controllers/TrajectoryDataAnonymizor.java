package controllers;

import models.RawDataModel;
import models.SensitiveDataModel;
import models.TrajectoryDataModel;
import services.QuasiDataService;
import services.RawDataService;
import services.SensitiveDataService;
import services.TrajectoryDataService;
import utils.Utils;

import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by Araja Jyothi Babu on 15-Apr-16.
 */
public class TrajectoryDataAnonymizor {

    //TODO:
    private int L;
    private int K;
    private int C;
    private int _K;
    private SensitiveDataModel S;

    //TODO:QuasiDataService quasiDataService;
    RawDataService rawDataService;
    SensitiveDataService sensitiveDataService;
    TrajectoryDataService trajectoryDataService;

    public static String generatePath(ArrayList<TrajectoryDataModel> doubletsWithId) throws Exception {
        SortedSet<TrajectoryDataModel> sortedDoubletsWithId = new TreeSet<TrajectoryDataModel>(doubletsWithId);
        ArrayList<String> doublets = new ArrayList();
        for(TrajectoryDataModel tdm : sortedDoubletsWithId){
            doublets.add(Utils.makeDoublet(tdm));
        }
        return Utils.makeSingleString(doublets);
    }

    public void updateRawData() throws Exception {
        rawDataService.emptyRawData(); //cleaning table for fresh data
        ArrayList<RawDataModel> rawDataModels = new ArrayList();
        ArrayList<SensitiveDataModel> sensitiveDataModels = sensitiveDataService.getSensitiveData();
        String path = "";
        for(SensitiveDataModel sensitiveData : sensitiveDataModels){
            path = generatePath(trajectoryDataService.getTrajectoryData(sensitiveData.id));
            rawDataModels.add(new RawDataModel(sensitiveData.id, path, sensitiveData.diagnosis));
        }
        rawDataService.insertRawData(rawDataModels);
    }

}
