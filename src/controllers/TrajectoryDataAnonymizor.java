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
    private static int L;  //maximum length of the background knowledge.
    private static int K;  //anonymity threshold
    private static double C;  //confidence threshold
    private static int _K; //minimum support threshold
    private static ArrayList<String> S;   //set of sensitive values of the sensitive attributes

    //TODO:QuasiDataService quasiDataService;
    static RawDataService rawDataService;
    static SensitiveDataService sensitiveDataService;
    static TrajectoryDataService trajectoryDataService;

    DB db;
    OracleDAO dao;

    //RawTrajectory Database
    static ArrayList<RawDataModel> T;

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
        updateRawData();    //updating T Database
        T = getRawData(); //getting T
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
        }
        rawDataService.insertRawData(rawDataModels);
    }

    private static ArrayList<RawDataModel> getRawData() throws Exception {
        ArrayList<RawDataModel> T = rawDataService.getRawData();
        return T;
    }

    private static ArrayList<RawDataModel> getRawData(ArrayList<String> S) throws Exception {
        ArrayList<RawDataModel> T = rawDataService.getRawData(S);
        return T;
    }

    //helper functions

    private static ArrayList<RawDataModel> T_withSequence(String m) throws Exception{
        ArrayList<RawDataModel> T = getRawData();
        Iterator<RawDataModel> iter = T.iterator();
        RawDataModel tuple;
        while(iter.hasNext()){
            tuple = iter.next();
            if(!areSubsets(tuple.getPath(), m))
                iter.remove();
        }
        return T;
    }

    private static boolean areSubsets(String path, String q) throws Exception {
        ArrayList<String> masterDoublets = Utils.arrayToArrayList(path.split("-"));
        ArrayList<String> childDoublets = Utils.arrayToArrayList(q.split("-"));
        for(String doublet : childDoublets){
            if(!masterDoublets.contains(doublet))
                return false;
        }
        return true;
    }

    private static double confidence(String s, ArrayList<RawDataModel> T_q){
        double conf = 0;
        double T_q_U_s = 0;
        double T_q_size = T_q.size();
        for(RawDataModel rawDataModel : T_q){
            if(rawDataModel.getDiagnosis().equals(s))
                T_q_U_s++; //counting frequency of both s and T_q
        }
        conf = T_q_U_s / T_q_size; //confidence
        return conf;
    }

    private static boolean confidenceForAll_s_In_S_to_C(ArrayList<RawDataModel> T_q){
        for(String s : S){
            if(confidence(s, T_q) > C)
                return false;
        }
        return true;
    }

    private static boolean confidenceForAll_s_In_subSetOfS_to_C(ArrayList<RawDataModel> T_q, ArrayList<String> subSetOfS){
        for(String s : subSetOfS){
            if(confidence(s, T_q) > C)
                return true;
        }
        return false;
    }

    private static boolean existenceOf_s_In_S_For_Sequence(ArrayList<RawDataModel> T_q){
        for(String s : S){
            if(confidence(s, T_q) > 0)
                return true;
        }
        return false;
    }

    private static boolean existenceOf_Sequence(String sequence, ArrayList<RawDataModel> T_q) throws Exception {
        for(RawDataModel tuple : T_q){
            if(areSubsets(tuple.getPath(), sequence))
                return true;
        }
        return false;
    }

    private static int frequencyOf_Sequence(String sequence, ArrayList<RawDataModel> T_q) throws Exception {
        int count = 0;
        for(RawDataModel tuple : T_q){
            if(areSubsets(tuple.getPath(), sequence))
                count++;
        }
        return count;
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
        return Integer.parseInt(doublet.replaceAll("[\\D]",""));
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
    public static ArrayList<String> minimalViolatingSequences() throws Exception{
        ArrayList<String> V_T = new ArrayList();
        ArrayList<ArrayList<String>> c = new ArrayList();
        c.add(trajectoryDataService.getAllUniqueDoublets()); //loading all distinct doublets
        int i = 1;
        ArrayList<ArrayList<String>> U = new ArrayList();
        ArrayList<ArrayList<String>> V = new ArrayList();
        ArrayList<RawDataModel> current_T_q = new ArrayList();
        while(i <= L && c.get(i-1) != null && c.get(i-1).size() > 0){
            //Utils.printList(c.get(i-1));
            U.add(new ArrayList<String>());
            V.add(new ArrayList<String>());
            for(String q : c.get(i-1)){
                current_T_q = T_withSequence(q);
                if(current_T_q.size() > 0){
                    if(current_T_q.size() >= K && confidenceForAll_s_In_S_to_C(current_T_q)){
                        U.get(i-1).add(q);
                    }else{
                        //FIXME: not entering sequences without having sensitive data S
                        if(existenceOf_s_In_S_For_Sequence(current_T_q))
                            V.get(i-1).add(q);
                    }
                }
            }
            i++;
            c.add(selfJoin(U.get(i-2)));
            Iterator<String> iter = c.get(i-1).iterator(); //to check ConcurrentModificationException
            while (iter.hasNext()) {
                String q = iter.next();
                for(String violatingSequence : V.get(i-2)) {
                    //if(q.contains(violatingSequence))
                    if (areSubsets(q, violatingSequence))
                        iter.remove();
                }
            }
        }
        V_T = unionOfSequences(V);
        /*System.out.println("Minimal Violating Sequences: ");
        Utils.printList(V_T);*/
        return V_T;
    }

    private static ArrayList<RawDataModel> T_p_withOut_T_m(ArrayList<RawDataModel> T_p, ArrayList<RawDataModel> T_m) throws Exception {
        ArrayList<RawDataModel> Tp_Tm = new ArrayList();
        for(RawDataModel tuple : T_m){
            if(!T_p.contains(tuple))
                Tp_Tm.add(tuple);
        }
        return Tp_Tm;
    }

    private static ArrayList<String> generateP(ArrayList<RawDataModel> T_m, ArrayList<RawDataModel> Tp_Tm) throws Exception{
        ArrayList<String> P = trajectoryDataService.getAllUniqueDoublets(); //initialize with all doublets
        Iterator<String> iter = P.iterator();
        String p;
        while (iter.hasNext()){
            p = iter.next();
            if(!(existenceOf_Sequence(p, T_m) && existenceOf_Sequence(p, Tp_Tm)))
                iter.remove();
        }
        return P;
    }

    private static ArrayList<String> generate_V(String p) throws Exception {
        ArrayList<String> mVS = minimalViolatingSequences(); //getting MVS
        ArrayList<String> _V = new ArrayList();
        for(String sequence : mVS){
            if(Utils.sizeOfSequence(sequence) == 1 || sequence.contains(p)){
                _V.add(sequence);
            }
        }
        return _V;
    }

    private static ArrayList<String> V_p(String p) throws Exception {
        ArrayList<String> mVS = minimalViolatingSequences(); //getting MVS
        ArrayList<String> Vp = new ArrayList();
        for(String sequence : mVS){
            if(sequence.contains(p)){
                Vp.add(sequence);
            }
        }
        return Vp;
    }

    private static ArrayList<String> Vt_Vp(String p) throws Exception {
        ArrayList<String> Vt = minimalViolatingSequences(); //getting MVS
        ArrayList<String> Vp = V_p(p);
        for(String sequence : Vt){
            if(Vp.contains(sequence)){
                Vt.remove(sequence);
            }
        }
        return Vt; //V(T) - V(p)
    }

    private static boolean isSuperSequenceForSequences(String child, ArrayList<String> list) throws Exception {
        for(String sequence : list){
            if(areSubsets(child, sequence))
                return true;
        }
        return false;
    }

    private static ArrayList<String> subSetOfS(ArrayList<RawDataModel> Tp_Tm){
        ArrayList<String> subSet = new ArrayList();
        for(RawDataModel tuple : Tp_Tm){
            if(S.contains(tuple.getDiagnosis()))
                subSet.add(tuple.getDiagnosis());
        }
        return subSet;
    }

    private static ArrayList<String> possibleSequencesFrom(ArrayList<String> doublets) throws Exception{
        ArrayList<String> possibleSequences = new ArrayList();
        for(String doublet : doublets){
            possibleSequences.add(doublet);
        }
        ArrayList<String> selfJoinedSequences = selfJoin(doublets);
        for(String sequence : selfJoinedSequences){
            possibleSequences.add(sequence);
        }
        return possibleSequences;
    }

    public static boolean isLocalSuppressionValid(String p, String m)  throws Exception {
        //1.
        ArrayList<RawDataModel> T_m = T_withSequence(m);
        ArrayList<RawDataModel> T_p = T_withSequence(p);
        ArrayList<RawDataModel> Tp_Tm = T_p_withOut_T_m(T_m, T_p);
        ArrayList<String> P = generateP(T_m, Tp_Tm);
        //2. generating V'
        ArrayList<String> _V = generate_V(p);
        //3. removing
        Iterator<String> iter_P = P.iterator();
        String pInP;
        while(iter_P.hasNext()){
            pInP = iter_P.next();
            //FIXME: didn't understand clearly what is the condition here. temporary fix
            if(!pInP.equals(p) && _V.contains(pInP)){
                iter_P.remove();
            }
        }
        //4.
        ArrayList<String> Q = possibleSequencesFrom(P);
        ArrayList<String> Vp = V_p(p);
        Iterator<String> iter_Q = Q.iterator();
        String qInQ;
        while(iter_P.hasNext()){
            qInQ = iter_Q.next();
            if(Utils.sizeOfSequence(qInQ) > L || isSuperSequenceForSequences(qInQ, Vp))
                Q.remove(qInQ);
        }
        //6.for each sequence q with |q| > 0 do
        ArrayList<String> subSet_S = subSetOfS(Tp_Tm);
        ArrayList<RawDataModel> Tq;
        for(String q : Q){
            Tq = T_withSequence(q);
            if(Utils.sizeOfSequence(q) < K || confidenceForAll_s_In_subSetOfS_to_C(Tq, subSet_S)) {
                return true;
            }
        }
        return false;
    }

    public static ArrayList<RawDataModel> anonymizedData() throws Exception {
        ArrayList<String> mVS = minimalViolatingSequences();
        //TODO: MFS Tree construction
        
    }

}
