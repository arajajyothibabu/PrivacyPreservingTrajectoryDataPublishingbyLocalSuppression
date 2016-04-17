package models;

/**
 * Created by Araja Jyothi Babu on 15-Apr-16.
 */
public class TrajectoryDataModel implements Comparable{

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrajectoryDataModel)) return false;

        TrajectoryDataModel that = (TrajectoryDataModel) o;

        return getTime().equals(that.getTime());

    }

    @Override
    public int hashCode() {
        return getTime().hashCode();
    }

    @Override
    public int compareTo(Object o) {
        TrajectoryDataModel that = (TrajectoryDataModel)o;
        return Integer.parseInt(this.time) - Integer.parseInt(that.getTime());
    }
}
