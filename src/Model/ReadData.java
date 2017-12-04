package Model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

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
    Integer signal;
    Boolean value;
    String timestamp;

    public ReadData(){
    }

    @JsonCreator
    public ReadData(@JsonProperty(ROBOT) String robot,
                    @JsonProperty(CLUSTER) String cluster,
                    @JsonProperty(ZONE) String zone,
                    @JsonProperty(SIGNAL) Integer signal,
                    @JsonProperty(VALUE) Boolean value,
                    @JsonProperty(TIMESTAMP) String timestamp) {
        this.robot = robot;
        this.cluster = cluster;
        this.zone = zone;
        this.signal = signal;
        this.value = value;
        this.timestamp = timestamp;
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

    public void setValue(boolean value) {
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
