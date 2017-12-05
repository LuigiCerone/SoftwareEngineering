package Model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.bson.BsonDocument;
import org.bson.BsonType;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;

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
    public static final String DOWN_TIME = "downTime";

    private String robotId;
    private String clusterId;
    private String zoneId;
    private float inefficiencyRate;

    private Signal[] robotSignals = new Signal[7];

    private int downTime;

    public Robot() {
    }

    @JsonCreator
    public Robot(@JsonProperty(ROBOT_ID) String robotId,
                 @JsonProperty(CLUSTER_ID) String clusterId,
                 @JsonProperty(ZONE_ID) String zoneId,
                 @JsonProperty(DOWN_TIME) int downTime,
                 @JsonProperty(SIGNALS) Signal[] signals) {
        this.robotId = robotId;
        this.clusterId = clusterId;
        this.zoneId = zoneId;
        this.robotSignals = signals;
        this.downTime = downTime;
    }

    public Robot(String robotId, String clusterId, String zoneId) {
        this.robotId = robotId;
        this.clusterId = clusterId;
        this.zoneId = zoneId;
        initRobotsSignals();
        this.downTime = 0;
    }

    // When a Robot is initialized all the signals are set to true-
    private void initRobotsSignals() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        String nowAsISO = df.format(new Date());
        for (int i = 0; i < 7; i++) {
            robotSignals[i] = new Signal(i + 1, true, nowAsISO);
        }
    }

    @JS
    public String getRobotId() {
        return robotId;
    }

    public void setRobotId(String robotId) {
        this.robotId = robotId;
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

//    public Signal[] getRobotSignals() {
//        return robotSignals;
//    }

    @Override
    public String toString() {
        return "Robot{" +
                "robotId='" + robotId + '\'' +
                ", clusterId='" + clusterId + '\'' +
                ", zoneId='" + zoneId + '\'' +
                ", inefficiencyRate=" + inefficiencyRate +
                ", robotSignals=" + Arrays.toString(robotSignals) +
                '}';
    }
}
