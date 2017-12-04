package Model;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

public class Robot {
    //JSON field names.
    public static final String ROBOT_ID = "robotId";
    public static final String CLUSTER_ID = "clusterId";
    public static final String ZONE_ID = "zoneId";
    public static final String SIGNALS = "signals";
    public static final String INEFFICIENCY_RATE = "IR";

    private String id;
    private String clusterId;
    private String zoneId;
    private float inefficiencyRate;
    private Signal[] robotSignals = new Signal[6];

    public Robot(){
    }

    @BsonCreator
    public Robot(@BsonProperty(ROBOT_ID) String id,
                 @BsonProperty(CLUSTER_ID) String clusterId,
                 @BsonProperty(ZONE_ID) String zoneId,
                 @BsonProperty(SIGNALS) Signal[] signals,
                 @BsonProperty(INEFFICIENCY_RATE) float inefficiencyRate) {
        this.id = id;
        this.clusterId = clusterId;
        this.zoneId = zoneId;
        this.inefficiencyRate = inefficiencyRate;
        this.robotSignals = signals;
    }

    public Robot(String id, String clusterId, String zoneId) {
        this.id = id;
        this.clusterId = clusterId;
        this.zoneId = zoneId;
        initRobotsSignals();
    }

    // When a Robot is initialized all the signals are set to true-
    private void initRobotsSignals() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        String nowAsISO = df.format(new Date());
        for (int i = 0; i < 7; i++) {
            robotSignals[i] = new Signal(i+1, true, nowAsISO);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public float getInefficiencyRate() {
        return inefficiencyRate;
    }

    public void setInefficiencyRate(float inefficiencyRate) {
        this.inefficiencyRate = inefficiencyRate;
    }

    @Override
    public String toString() {
        return "Robot{" +
                "id='" + id + '\'' +
                ", clusterId='" + clusterId + '\'' +
                ", zoneId='" + zoneId + '\'' +
                ", inefficiencyRate=" + inefficiencyRate +
                ", robotSignals=" + Arrays.toString(robotSignals) +
                '}';
    }
}
