package Model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Robot {
    private String id;
    private String clusterId;
    private String zoneId;
    private boolean isUp;
    private float inefficiencyRate;
    private Signal[] robotSignals = new Signal[6];


    public Robot(String id, String clusterId, String zoneId, boolean isUp, float inefficiencyRate) {
        this.id = id;
        this.clusterId = clusterId;
        this.zoneId = zoneId;
        this.isUp = isUp;
        this.inefficiencyRate = inefficiencyRate;
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

    public boolean isUp() {
        return isUp;
    }

    public void setUp(boolean up) {
        isUp = up;
    }

    public float getInefficiencyRate() {
        return inefficiencyRate;
    }

    public void setInefficiencyRate(float inefficiencyRate) {
        this.inefficiencyRate = inefficiencyRate;
    }
}
