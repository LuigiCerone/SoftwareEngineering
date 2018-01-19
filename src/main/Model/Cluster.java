package main.Model;

import com.google.gson.annotations.Expose;
import org.bson.Document;

import java.sql.Timestamp;
import java.util.HashMap;

public class Cluster {
    //JSON and DatabaseConnector field names.
    public static final String CLUSTER_ID = "_id";
    public static final String ZONE_ID = "zoneId";
    public static final String INEFFICIENCY_RATE = "ir";
    public static final String DOWN_TIME = "downTime";
    public static final String COUNT_INEFFICIENCY_COMPONENTS = "count";
    public static final String START_DOWN_TIME = "startDownTime";
    public static final String START_UP_TIME = "startUpTime";

    @Expose
    private String clusterId;
    @Expose
    private Double inefficiencyRate;
    @Expose
    private String zoneId;

    private int countInefficiencyComponents;
    private int downTime;
    private Timestamp startUpTime;
    private Timestamp startDownTime;

    @Expose
    private HashMap<String, Robot> robotsList;

    public Cluster() {
    }

    public Cluster(String clusterId, String zoneId, double ir) {
        this.clusterId = clusterId;
        this.zoneId = zoneId;
        this.inefficiencyRate = ir;
        this.robotsList = new HashMap<String, Robot>();
    }


    public Cluster(Document document) {
        clusterId = document.getString(Cluster.CLUSTER_ID);
        zoneId = document.getString(Cluster.ZONE_ID);
        inefficiencyRate = document.getDouble(Cluster.INEFFICIENCY_RATE);
        countInefficiencyComponents = document.getInteger(Cluster.COUNT_INEFFICIENCY_COMPONENTS);
        downTime = document.getInteger(Cluster.DOWN_TIME);
        startUpTime = new Timestamp(document.getLong(Cluster.START_UP_TIME));
        try {
            startDownTime = new Timestamp(document.getLong(Cluster.START_DOWN_TIME));
        } catch (Exception e) {
            startDownTime = null;
        }

    }

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public double getInefficiencyRate() {
        return inefficiencyRate;
    }

    public void setInefficiencyRate(Double inefficiencyRate) {
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

    public void addRobot(String robotId, Robot r) {
        this.robotsList.put(robotId, r);
    }

    public void setRobotsList(HashMap<String, Robot> robotsList) {
        this.robotsList = robotsList;
    }

    public Robot getRobot(String robotId) {
        return robotsList.get(robotId);
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
