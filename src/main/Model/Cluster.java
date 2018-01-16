package main.Model;

import com.google.gson.annotations.Expose;

import java.sql.Timestamp;
import java.util.HashSet;

public class Cluster {
    //JSON and Database field names.
    public static final String CLUSTER_ID = "id";
    public static final String ZONE_ID = "zoneId";
    public static final String INEFFICIENCY_RATE = "ir";
    public static final String DOWN_TIME = "downTime";
    public static final String COUNT_INEFFICIENCY_COMPONENTS = "count";
    public static final String START_DOWN_TIME = "startDownTime";
    public static final String START_UP_TIME = "startUpTime";

    @Expose
    private String clusterId;
    @Expose
    private float inefficiencyRate;
    @Expose
    private String zoneId;

    private int countInefficiencyComponents;
    private int downTime;
    private Timestamp startUpTime;
    private Timestamp startDownTime;

    @Expose
    private HashSet<Robot> robotsList;

    public Cluster() {
    }

    public Cluster(String clusterId, String zoneId, float ir) {
        this.clusterId = clusterId;
        this.zoneId = zoneId;
        this.inefficiencyRate = ir;
        this.robotsList = new HashSet<>();
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

    public void addRobot(Robot r) {
        this.robotsList.add(r);
    }

    @Override
    public boolean equals(Object obj) {
        return this.clusterId.equals(((Cluster) obj).getClusterId());
    }

    @Override
    public String toString() {
        return " {" +
                "\"clusterId\":\" " + clusterId + "\"" +
                ", \"inefficiencyRate\": " + inefficiencyRate +
                ", \"zoneId\": \"" + zoneId + "\"" +
                ", \"robotsList\": " + robotsList +
                "}";
    }
}
