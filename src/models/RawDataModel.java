package models;

/**
 * Created by Araja Jyothi Babu on 16-Apr-16.
 */
public class RawDataModel {

    int id;
    String path;
    String diagnosis;
    //TODO: extend with further details of patient or any

    public RawDataModel(int id, String path, String diagnosis) {
        this.id = id;
        this.path = path;
        this.diagnosis = diagnosis;
    }

    public RawDataModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RawDataModel)) return false;

        RawDataModel that = (RawDataModel) o;

        return getId() == that.getId();

    }

    @Override
    public int hashCode() {
        return getId();
    }
}
