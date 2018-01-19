package main.Model;

import com.google.gson.annotations.Expose;
import org.bson.Document;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;

public class Cluster {
    //JSON and DatabaseConnector field names.
    public static final String CLUSTER_ID = "_id";
    public static final String ZONE_ID = "zoneId";
    public static final String INEFFICIENCY_RATE = "ir";
    public static final String COUNT_INEFFICIENCY_COMPONENTS = "count";
    public static final String START_DOWN_TIME = "startDownTime";
    public static final String START_UP_TIME = "startUpTime";
    public static final String HISTORY = "history";
    public static final String HISTORIES = "histories";

    @Expose
    private String clusterId;
    @Expose
    private Double inefficiencyRate;
    @Expose
    private String zoneId;

    private int countInefficiencyComponents;
    private Timestamp startUpTime;
    private Timestamp startDownTime;

    @Expose
    private HashSet<Robot> robotsList;

    ArrayList<History> histories;

    public Cluster() {
    }

    public Cluster(String clusterId, String zoneId, double ir) {
        this.clusterId = clusterId;
        this.zoneId = zoneId;
        this.inefficiencyRate = ir;
        this.robotsList = new HashSet<Robot>();
    }


    public Cluster(Document document) {
        clusterId = document.getString(Cluster.CLUSTER_ID);
        zoneId = document.getString(Cluster.ZONE_ID);
        inefficiencyRate = document.getDouble(Cluster.INEFFICIENCY_RATE);
        countInefficiencyComponents = document.getInteger(Cluster.COUNT_INEFFICIENCY_COMPONENTS);
        startUpTime = new Timestamp(document.getLong(Cluster.START_UP_TIME));
        try {
            startDownTime = new Timestamp(document.getLong(Cluster.START_DOWN_TIME));
        } catch (Exception e) {
            startDownTime = null;
        }

//        try {
//            histories = (ArrayList<History>) document.get(Cluster.HISTORIES);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
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

    public void setRobotsList(HashSet<Robot> robotsList) {
        this.robotsList = robotsList;
    }

    public HashSet<Robot> getRobotsList() {
        return robotsList;
    }

    @Override
    public boolean equals(Object obj) {
        return this.clusterId.equals(((Cluster) obj).getClusterId());
    }

    public void setHistories(ArrayList<Document> historiesDoc) {
        ArrayList<History> histories = new ArrayList<>(historiesDoc.size());
        for (Document document : historiesDoc) {
            histories.add(new History(document));
        }
        this.histories = histories;
    }

    public ArrayList<History> getHistories() {
        return histories;
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
