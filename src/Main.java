import controllers.TrajectoryDataAnonymizor;
import models.SensitiveDataModel;
import utils.DB;

import java.util.ArrayList;

/**
 * Created by Araja Jyothi Babu on 15-Apr-16.
 */
public class Main {

    public static void main(String args[]) throws Exception {

        DB db = new DB();
        int L = 2;  //maximum length of the background knowledge.
        int K = 2;  //anonymity threshold
        double C = 0.5;  //confidence threshold
        int _K = 1; //minimum support threshold
        ArrayList<String> S = new ArrayList();   //set of sensitive values of the sensitive attributes

        S.add("HIV");
        S.add("Hepatitis");

        TrajectoryDataAnonymizor trajectoryDataAnonymizor = new TrajectoryDataAnonymizor(L, K, C, _K, S, db);
        trajectoryDataAnonymizor.minimalViolatingSequences();
    }

}
