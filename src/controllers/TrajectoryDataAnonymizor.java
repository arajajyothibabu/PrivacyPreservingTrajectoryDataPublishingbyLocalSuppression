package controllers;

import models.*;
import services.*;
import utils.*;

import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by Araja Jyothi Babu on 15-Apr-16.
 */
public class TrajectoryDataAnonymizor {

    //TODO:
    private int L;  //maximum length of the background knowledge.
    private int K;  //anonymity threshold
    private double C;  //confidence threshold
    private int _K; //minimum support threshold
    private ArrayList<String> S;   //set of sensitive values of the sensitive attributes

    //TODO:QuasiDataService quasiDataService;
    RawDataService rawDataService;
    SensitiveDataService sensitiveDataService;
    TrajectoryDataService trajectoryDataService;

    DB db;
    OracleDAO dao;

    public TrajectoryDataAnonymizor(int l, int k, double c, int _K, ArrayList<String> s, DB db) throws Exception {
        L = l;
        K = k;
        C = c;
        this._K = _K;
        S = s;
        this.db = db;
        dao = new OracleDAO(db);
        rawDataService = new RawDataService(dao);
        sensitiveDataService = new SensitiveDataService(dao);
        trajectoryDataService = new TrajectoryDataService(dao);
    }

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
