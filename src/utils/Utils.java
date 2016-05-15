package utils;

import models.RawDataModel;
import models.SensitiveDataModel;
import models.TrajectoryDataModel;

import java.sql.ResultSet;
import java.util.*;

/**
 * Created by Araja Jyothi Babu on 15-Apr-16.
 */
public class Utils {
    //global functions for use all over project

    public static SensitiveDataModel makeSensitiveDataModel(ResultSet rs) throws Exception{
        return new SensitiveDataModel(rs.getInt(1), rs.getString(2));
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

    public static String makeDoublet(ResultSet rs) throws Exception {
        return rs.getString(1) + rs.getString(2);
    }

    public static String makeSingleString(ArrayList<String> doublets) throws Exception {
        StringBuilder path = new StringBuilder("");
        int length = doublets.size();
        for(String doublet : doublets){
            path.append(doublet + (length != 1 ? "-" : ""));
            length--;
        }
        return path.toString();
    }

    public static String makeSingleString(ArrayList<String> doublets, int except) throws Exception {
        StringBuilder path = new StringBuilder("");
        int length = except;
        for(String doublet : doublets){
            path.append(doublet + (length != 1 ? "-" : ""));
            length--;
        }
        return path.toString();
    }

    public static ArrayList<String> uniqueList(ArrayList<String> strings){
        ArrayList<String> uniqueList = new ArrayList();
        HashSet<String> set = new HashSet(strings);
        for(String s : set){
            uniqueList.add(s);
        }
        return uniqueList;
    }

    public static ArrayList<String> arrayToArrayList(String[] array){
        ArrayList<String> strings = new ArrayList();
        for(String s : array){
            strings.add(s);
        }
        return strings;
    }

    public static void printList(ArrayList<String> list){
        for(String s : list)
            System.out.print(">> " + s);
        System.out.println("\n_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-");
    }

    public static void printPath(ArrayList<RawDataModel> list){
        for(RawDataModel rdm : list)
            System.out.print(">> " + rdm.getPath());
        System.out.println("\n_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-");
    }

    public static int sizeOfSequence(String sequence){
        return sequence.split("-").length;
    }

    public static boolean areSubsets(String path, String q) throws Exception {
        ArrayList<String> masterDoublets = Utils.arrayToArrayList(path.split("-"));
        ArrayList<String> childDoublets = Utils.arrayToArrayList(q.split("-"));
        for(String doublet : childDoublets){
            if(!masterDoublets.contains(doublet))
                return false;
        }
        return true;
    }

    public static boolean areSubsets(String path, ArrayList<String> q) throws Exception {
        ArrayList<String> masterDoublets = Utils.arrayToArrayList(path.split("-"));
        for(String doublet : q){
            if(!masterDoublets.contains(doublet))
                return false;
        }
        return true;
    }

    public static boolean areSubsets(ArrayList<String> path, ArrayList<String> q) throws Exception {
        for(String doublet : q){
            if(!path.contains(doublet))
                return false;
        }
        return true;
    }

    public static int frequencyOf_Sequence(String sequence, ArrayList<RawDataModel> T_q) throws Exception {
        int count = 0;
        for(RawDataModel tuple : T_q){
            if(areSubsets(tuple.getPath(), sequence))
                count++;
        }
        return count;
    }

    public static int frequencyOf_Sequence(ArrayList<String> sequence, ArrayList<RawDataModel> T_q) throws Exception {
        int count = 0;
        for(RawDataModel tuple : T_q){
            if(areSubsets(tuple.getPath(), sequence))
                count++;
        }
        return count;
    }

    public static String digitsFromString(String str){
        return str.replaceAll("\\D+","");
    }

    public static int numberFromString(String str){
        return Integer.valueOf(digitsFromString(str));
    }

    public static boolean isSubsetOfAny(ArrayList<String> sequence, SortedMap<String, HashMap<ArrayList<String>, Integer>> mvs) throws Exception{
        Collection<HashMap<ArrayList<String>, Integer>> keys = mvs.values();
        for(HashMap<ArrayList<String>, Integer> map : keys){
            for(ArrayList<String> seq : map.keySet()){
                System.out.println("first-------");printList(seq);
                if(areSubsets(seq, sequence)){
                    return true;
                }
            }
        }
        return false;
    }

}
