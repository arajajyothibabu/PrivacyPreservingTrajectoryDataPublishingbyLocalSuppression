package controllers;

import models.RawDataModel;
import utils.Utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

import static utils.Utils.*;

/**
 * Created by Araja Jyothi Babu on 12-May-16.
 */
public class MFS {

    private int minSupportCount;
    ArrayList<String> listOfItems;
    ArrayList<RawDataModel> data;

    public MFS(int minSupportCount, ArrayList<String> items, ArrayList<RawDataModel> data) {
        this.minSupportCount = minSupportCount;
        listOfItems = items;
        this.data = data;
        generateItemSets();
    }

    private static int pow(int a,int b) {
        int mul=1;
        for(;b>0;b--)
            mul *= a;
        return mul;
    }

    private static int len(char arr[]) {
        int i=0;
        for(;arr[i]!='\0';i++);
        return i;
    }

    private static char[] Bin(int n,int length) {
        char []tempArr = new char[length];
        while(n>0)
        {
            tempArr[--length] = (char)(n%2+48);
            n /= 2;
        }
        return tempArr;
    }

    private static ArrayList<ArrayList<String>> combinations(ArrayList<String> arr) {
        int size = arr.size();
        char []binary = new char[size];
        int range = pow(2,size),index = 0;
        ArrayList<ArrayList<String>> output = new ArrayList();
        ArrayList<String> tempList;
        for(int i=1;i<range;i++) {
            binary = Bin(i, size);
            tempList = new ArrayList();
            for(int j = 0; j < size; j++) {
                if(binary[j] == '1')
                    tempList.add(index++, arr.get(j));
            }
            output.add(i-1, tempList);
            index = 0;
        }
        return output;
    }

    void generateItemSets() {
        int N = listOfItems.size();
        ArrayList<ArrayList<String>> Combinations = combinations(listOfItems); //storing all combinations
        HashMap<ArrayList<String>,Integer> candidateNItemSet = new HashMap<ArrayList<String>,Integer>();
        SortedMap<String, HashMap<ArrayList<String>, Integer>> mvs = new TreeMap<String, HashMap<ArrayList<String>, Integer>>();
        try {
            int frequency = 0;
            ArrayList<ArrayList<String>> collectedSequences = new ArrayList();
            while(true) {
                for(ArrayList<String> combination : Combinations) {
                    //printList(combination);
                    //System.out.print(isSubsetOfAny(combination, mvs));
                    frequency = frequencyOf_Sequence(combination, data);
                    if(combination.size() == N && frequency > 1 && !isSubsetOfAny(combination, collectedSequences)) {
                        collectedSequences.add(combination);
                        candidateNItemSet.put(combination, frequency);
                    }
                }
                if(N == 0)
                    break;
                /*System.out.println("Candidte-"+N+"-ItemSet");
                for(Map.Entry me : candidateNItemSet.entrySet())
                {
                    System.out.println(">>"+me.getKey()+"\t"+me.getValue());
                }*/
                System.out.println("Frequent-"+N+"-ItemSet");
                for(Map.Entry me : candidateNItemSet.entrySet()) {
                    if((Integer)me.getValue() >= minSupportCount)
                        System.out.println(">>"+me.getKey()+"\t"+me.getValue());
                }
                if(candidateNItemSet.size() > 0 )
                    mvs.put(String.valueOf(N), candidateNItemSet);
                candidateNItemSet.clear();
                System.out.println("*************************************************");
                N--; //incrementing N-candidateSet
            }
        }
        catch(Exception e) {
            System.out.println("Error: "+ e);
        }
    }

}