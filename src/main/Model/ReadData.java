package main.Model;


import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    boolean value;
    Timestamp timestamp;

    public ReadData() {
    }

    public ReadData(String readDataToDeserialize) {
        JSONObject jsonObject = new JSONObject(readDataToDeserialize);
        this.robot = jsonObject.getString(ROBOT);
        this.cluster = jsonObject.getString(CLUSTER);
        this.zone = jsonObject.getString(ZONE);
        this.signal = jsonObject.getInt(SIGNAL);
//        if (jsonObject.getInt(VALUE) == 0 || jsonObject.getBoolean(VALUE) == false) this.value = false;
//        else this.value = true;
        this.value = jsonObject.getBoolean(VALUE);
        this.timestamp = formatTimestamp(jsonObject.getString(TIMESTAMP));
    }

    private Timestamp formatTimestamp(String timestampAsString) {
        Timestamp toTimestamp = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date parsedDate = dateFormat.parse(timestampAsString);
            toTimestamp = new Timestamp(parsedDate.getTime());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return toTimestamp;
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

    public boolean getValue() {
        return value;
    }

    public Timestamp getTimestamp() {
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

    public void setValue(boolean value) {
        this.value = value;
    }

    public void setTimestamp(Timestamp timestamp) {
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
