package Model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ReadData {
    // JSON field name
    public static final String ROBOT = "robot";
    public static final String CLUSTER = "cluster";
    public static final String ZONE = "zone";
    public static final String SIGNAL = "signal";
    public static final String VALUE = "value";
    public static final String TIMESTAMP = "timestamp";


    private String robot;
    private String cluster;
    private String zone;
    private int signal;
    private boolean value;
    private String timestamp;


    public ReadData(){

    }

//    public ReadData(String robot, String cluster, String zone, int signal, boolean value, String timestamp) {
//        this.robot = robot;
//        this.cluster = cluster;
//        this.zone = zone;
//        this.signal = signal;
//        this.value = value;
//        this.timestamp = timestamp;
//    }

    @JsonCreator
    public ReadData(@JsonProperty(ROBOT) String robot,
                    @JsonProperty(CLUSTER) String cluster,
                    @JsonProperty(ZONE) String zone,
                    @JsonProperty(SIGNAL) int signal,
                    @JsonProperty(VALUE) boolean value,
                    @JsonProperty(TIMESTAMP) String timestamp){
        this.robot = robot;
        this.cluster = cluster;
        this.zone = zone;
        this.signal = signal;
        this.value = value;
        this.timestamp = timestamp;
    }

    @JsonProperty(ROBOT)
    public String getRobot() {
        return robot;
    }

    @JsonProperty(CLUSTER)
    public String getCluster() {
        return cluster;
    }

    @JsonProperty(ZONE)
    public String getZone() {
        return zone;
    }

    @JsonProperty(SIGNAL)
    public int getSignal() {
        return signal;
    }

    @JsonProperty(VALUE)
    public boolean getValue() {
        return value;
    }

    @JsonProperty(TIMESTAMP)
    public String getTimestamp() {
        return timestamp;
    }
}
