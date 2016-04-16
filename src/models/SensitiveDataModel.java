package models;

/**
 * Created by Araja Jyothi Babu on 15-Apr-16.
 */
public class SensitiveDataModel {

    //TODO: have to implement if required
    public String diagnosis;

    public SensitiveDataModel(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }
}
