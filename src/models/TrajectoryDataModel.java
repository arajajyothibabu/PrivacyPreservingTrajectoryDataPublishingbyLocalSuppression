package models;

/**
 * Created by Araja Jyothi Babu on 15-Apr-16.
 */
public class TrajectoryDataModel {

    int id;
    String location;
    String time;

    public TrajectoryDataModel(int id, String location, String time) {
        this.id = id;
        this.location = location;
        this.time = time;
    }

    public TrajectoryDataModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
