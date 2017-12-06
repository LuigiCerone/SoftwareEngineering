package Model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.json.JSONObject;

public class ReadData {
    // JSON field name.
    public static final String ROBOT = "robot";
    public static final String CLUSTER = "cluster";
    public static final String ZONE = "zone";
    public static final String SIGNAL = "signal";
    public static final String VALUE = "value";
    public static final String TIMESTAMP = "timestamp";

    String robot;
    String cluster;
    String zone;
    int signal;
    int value;
    String timestamp;

    public ReadData(){
    }

    public ReadData(String readDataToDeserialize) {
        JSONObject jsonObject = new JSONObject(readDataToDeserialize);
        this.robot = jsonObject.getString(ROBOT);
        this.cluster = jsonObject.getString(CLUSTER);
        this.zone = jsonObject.getString(ZONE);
        System.out.println("Nel costruttore");

        this.signal = jsonObject.getInt(SIGNAL);
        this.value = jsonObject.getInt(VALUE);
        // this.timestamp = jsonObject.getString(TIMESTAMP);
    }


    public String getRobot() {
        return robot;
    }

    public String getCluster() {
        return cluster;
    }

    public String getZone() {
        return zone;
    }

    public int getSignal() {
        return signal;
    }

    public int getValue() {
        return value;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setRobot(String robot) {
        this.robot = robot;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public void setSignal(int signal) {
        this.signal = signal;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ReadData{" +
                "robot='" + robot + '\'' +
                ", cluster='" + cluster + '\'' +
                ", zone='" + zone + '\'' +
                ", signal=" + signal +
                ", value=" + value +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
