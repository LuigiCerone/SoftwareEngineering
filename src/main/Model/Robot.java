package main.Model;

import com.google.gson.annotations.Expose;
import org.bson.Document;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

public class Robot {
    //JSON and DatabaseConnector field names.
    public static final String ROBOT_ID = "_id";
    public static final String CLUSTER_ID = "clusterId";
    public static final String INEFFICIENCY_RATE = "ir";
    public static final String COUNT_INEFFICIENCY_COMPONENTS = "count";
    public static final String START_DOWN_TIME = "startDownTime";
    public static final String START_UP_TIME = "startUpTime";
    public static final String HISTORY = "history";
    public static final String HISTORIES = "histories";
    public static final String SIGNALS = "signals";

    @Expose
    private String robotId;
    @Expose
    private String clusterId;
    @Expose
    private Double inefficiencyRate;

    private int countInefficiencyComponents;

    private HashMap<Integer, Boolean> robotSignals = new HashMap<Integer, Boolean>();
    private Timestamp startUpTime;
    private Timestamp startDownTime;
    private ArrayList<History> histories;

    public Robot() {
    }

    public Robot(String robotId, String clusterId, double inefficiencyRate) {
        this.robotId = robotId;
        this.clusterId = clusterId;
        this.inefficiencyRate = inefficiencyRate;
    }

    public Robot(Document document) {
        robotId = document.getString(Robot.ROBOT_ID);
        clusterId = document.getString(Robot.CLUSTER_ID);
        inefficiencyRate = document.getDouble(Robot.INEFFICIENCY_RATE);
        countInefficiencyComponents = document.getInteger(Robot.COUNT_INEFFICIENCY_COMPONENTS);
        startUpTime = new Timestamp(document.getLong(Robot.START_UP_TIME));
        try {
            startDownTime = new Timestamp(document.getLong(Robot.START_DOWN_TIME));
        } catch (Exception e) {
            startDownTime = null;
        }
    }

    public void setCountInefficiencyComponents(int countInefficiencyComponents) {
        this.countInefficiencyComponents = countInefficiencyComponents;
    }

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

    public double getInefficiencyRate() {
        return inefficiencyRate;
    }

    public void setInefficiencyRate(Double inefficiencyRate) {
        this.inefficiencyRate = inefficiencyRate;
    }

    public void setRobotSignals(HashMap<Integer, Boolean> robotSignals) {
        this.robotSignals = robotSignals;
    }

    public Boolean getRobotSignal(int i) {
        return robotSignals.get(i);
    }

    public int getCountInefficiencyComponents() {
        return countInefficiencyComponents;
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

    public ArrayList<History> getHistories() {
        return histories;
    }

    public int updateComponentState(boolean value) {
        // The reading contains a down signal from this robot.
        if (!value) {
            this.countInefficiencyComponents--;
//            System.out.println("A component of this robot is not working.");
        } else if (value) {
            this.countInefficiencyComponents++;
//            System.out.println("A component of this robot is working.");
        } else {
//            System.out.println("Something strange has just happened.");
        }
        return this.countInefficiencyComponents;
    }

    @Override
    public boolean equals(Object obj) {
        Robot a = (Robot) obj;
        return this.robotId.equals(a.getRobotId());
    }

    public void setHistories(ArrayList<Document> historiesDoc) {
        ArrayList<History> histories = new ArrayList<>(historiesDoc.size());
        for (Document document : historiesDoc) {
            histories.add(new History(document));
        }
        this.histories = histories;
    }

    @Override
    public String toString() {
        return " {" +
                "\"robotId\": \"" + robotId + "\"" +
                ", \"inefficiencyRate\": " + inefficiencyRate +
                "}";
    }
}
