package controllers;

import models.*;
import services.*;
import utils.*;

import java.util.*;

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
    static RawDataService rawDataService;
    static SensitiveDataService sensitiveDataService;
    static TrajectoryDataService trajectoryDataService;

    DB db;
    OracleDAO dao;

    //RawTrajectory Database
    ArrayList<RawDataModel> rawData;

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
        updateRawData();    //updating rawData Database
        rawData = getRawData(); //getting rawData
    }

    public static String generatePath(ArrayList<TrajectoryDataModel> doubletsWithId) throws Exception {
        SortedSet<TrajectoryDataModel> sortedDoubletsWithId = new TreeSet<TrajectoryDataModel>(doubletsWithId);
        ArrayList<String> doublets = new ArrayList();
        for(TrajectoryDataModel tdm : sortedDoubletsWithId){
            doublets.add(Utils.makeDoublet(tdm));
        }
        return Utils.makeSingleString(doublets);
    }

    private void updateRawData() throws Exception {
        rawDataService.emptyRawData(); //cleaning table for fresh data
        ArrayList<RawDataModel> rawDataModels = new ArrayList();
        ArrayList<SensitiveDataModel> sensitiveDataModels = sensitiveDataService.getSensitiveData();
        String path = "";
        for(SensitiveDataModel sensitiveData : sensitiveDataModels){
            path = generatePath(trajectoryDataService.getTrajectoryData(sensitiveData.id));
            rawDataModels.add(new RawDataModel(sensitiveData.id, path, sensitiveData.diagnosis));
            System.out.println(sensitiveData.diagnosis);
        }
        rawDataService.insertRawData(rawDataModels);
    }

    private static ArrayList<RawDataModel> getRawData() throws Exception {
        ArrayList<RawDataModel> rawData = rawDataService.getRawData();
        return rawData;
    }

    //helper functions

    private ArrayList<RawDataModel> T_q(String q){
        ArrayList<RawDataModel> recordsWithSequence_q = new ArrayList();
        for(RawDataModel rawDataModel : rawData){
            if(rawDataModel.getPath().contains(q))
                recordsWithSequence_q.add(rawDataModel);
        }
        return recordsWithSequence_q;
    }

    private double confidence(String s, ArrayList<RawDataModel> T_q){
        double conf = 0;
        double T_q_U_s = 0;
        double T_q_size = T_q.size();
        for(RawDataModel rawDataModel : T_q){
            if(rawDataModel.getDiagnosis().equals(s))
                T_q_U_s++; //counting frequency of both s and T_q
        }
        return T_q_U_s / T_q_size; //confidence
    }

    private boolean confidenceForAll_s_In_S_to_C(ArrayList<String> S, ArrayList<RawDataModel> T_q){
        for(String s : S){
            if(confidence(s, T_q) > C)
                return false;
        }
        return true;
    }

    private static boolean areEqualExceptLastOne(ArrayList<String> qx, ArrayList<String> qy){
        int qxSize = qx.size();
        int qySize = qx.size();
        if(qxSize == qySize){
            for(int i = 0; i < qxSize-1 ; i++)
                if(!qx.get(i).equals(qy.get(i)))
                    return false;
        }
        return true;
    }

    private static int timeFromDoublet(String doublet){
        return Integer.parseInt(doublet.replaceAll("^[0-9]",""));
    }

    private static ArrayList<String> selfJoin(ArrayList<String> U_i) throws Exception {
        ArrayList<String> c = new ArrayList();
        ArrayList<String> qx = new ArrayList(), qy = new ArrayList();
        for(String q_X : U_i){
            qx = Utils.arrayToArrayList(q_X.split("-"));
            for(String q_Y : U_i){
                qy = Utils.arrayToArrayList(q_Y.split("-"));
                if(areEqualExceptLastOne(qx,qy) && timeFromDoublet(qx.get(qx.size()-1)) < timeFromDoublet(qy.get(qy.size()-1)))
                    c.add(Utils.makeSingleString(qx) + "-" + qy.get(qy.size()-1));
            }
        }
        c = Utils.uniqueList(c);
        return c;
    }

    private static ArrayList<String> unionOfSequences(ArrayList<ArrayList<String>> violatingSequences){
        ArrayList<String> uniqueList = new ArrayList();
        HashSet<String> set = new HashSet<String>();
        for(ArrayList<String> sequence : violatingSequences){
            for(String q : sequence){
                set.add(q);
            }
        }
        for(String s : set){
            uniqueList.add(s);
        }
        return uniqueList;
    }

    //Minimal Violating Sequences (MVS)
    public ArrayList<String> minimalViolatingSequences() throws Exception{
        ArrayList<String> V_T = new ArrayList();
        ArrayList<ArrayList<String>> c = new ArrayList();
        c.add(trajectoryDataService.getAllUniqueDoublets()); //loading all distinct doublets
        int i = 1;
        ArrayList<ArrayList<String>> U = new ArrayList();
        ArrayList<ArrayList<String>> V = new ArrayList();
        ArrayList<RawDataModel> current_T_q = new ArrayList();
        while(i <= L && c.get(i-1) != null){
            U.add(new ArrayList<String>());
            V.add(new ArrayList<String>());
            for(String q : c.get(i-1)){
                current_T_q = T_q(q);
                if(current_T_q.size() > 0){
                    if(current_T_q.size() >= K && confidenceForAll_s_In_S_to_C(S, current_T_q)){
                        U.get(i-1).add(q);
                    }else{
                        V.get(i-1).add(q);
                    }
                }
            }
            i++;
            c.add(selfJoin(U.get(i-2)));
            for(String q: c.get(i-1)){
                for(String violatingSequences : V.get(i-2))
                    if(q.contains(violatingSequences))
                        c.get(i-1).remove(q);
            }
        }
        V_T = unionOfSequences(V);
        return V_T;
    }

}
