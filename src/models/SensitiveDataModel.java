package models;

/**
 * Created by Araja Jyothi Babu on 15-Apr-16.
 */
public class SensitiveDataModel {

    //TODO: have to implement if required
    public int id;
    public String diagnosis;

    public SensitiveDataModel(int id, String diagnosis) {
        this.id = id;
        this.diagnosis = diagnosis;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }
}
