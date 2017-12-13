package main.Model;

import java.sql.Timestamp;
import java.util.List;

public class Cluster {
    //JSON and Database field names.
    public static final String CLUSTER_ID = "id";
    public static final String INEFFICIENCY_RATE = "ir";
    public static final String DOWN_TIME = "downTime";
    public static final String COUNT_INEFFICIENCY_COMPONENTS = "count";
    public static final String START_DOWN_TIME = "startDownTime";
    public static final String START_UP_TIME = "startUpTime";

    private String clusterId;
    private float inefficiencyRate;
    private int countInefficiencyComponents;
    private int downTime;
    private Timestamp startUpTime;
    private Timestamp startDownTime;
    private String zoneId;
    private List<Robot> robotsList;

    public Cluster() {
    }

    public Cluster(String clusterId, float inefficiencyRate, int countInefficiencyComponents, int downTime, Timestamp startUpTime, Timestamp startDownTime, String zoneId, List<Robot> robotsList) {
        this.clusterId = clusterId;
        this.inefficiencyRate = inefficiencyRate;
        this.countInefficiencyComponents = countInefficiencyComponents;
        this.downTime = downTime;
        this.startUpTime = startUpTime;
        this.startDownTime = startDownTime;
        this.zoneId = zoneId;
        this.robotsList = robotsList;
    }


    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public float getInefficiencyRate() {
        return inefficiencyRate;
    }

    public void setInefficiencyRate(float inefficiencyRate) {
        this.inefficiencyRate = inefficiencyRate;
    }

    public int getCountInefficiencyComponents() {
        return countInefficiencyComponents;
    }

    public void setCountInefficiencyComponents(int countInefficiencyComponents) {
        this.countInefficiencyComponents = countInefficiencyComponents;
    }

    public int getDownTime() {
        return downTime;
    }

    public void setDownTime(int downTime) {
        this.downTime = downTime;
    }

    public Timestamp getStartUpTime() {
        return startUpTime;
    }

    public void setStartUpTime(Timestamp startUpTime) {
        this.startUpTime = startUpTime;
    }

    public Timestamp getStartDownTime() {
        return startDownTime;
    }

    public void setStartDownTime(Timestamp startDownTime) {
        this.startDownTime = startDownTime;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }
}
