package main.Model;

import com.google.gson.annotations.Expose;
import org.bson.Document;

import java.sql.Timestamp;
import java.util.HashMap;

public class Robot {
    //JSON and DatabaseConnector field names.
    public static final String ROBOT_ID = "_id";
    public static final String CLUSTER_ID = "clusterId";
    public static final String INEFFICIENCY_RATE = "ir";
    public static final String DOWN_TIME = "downTime";
    public static final String COUNT_INEFFICIENCY_COMPONENTS = "count";
    public static final String START_DOWN_TIME = "startDownTime";
    public static final String START_UP_TIME = "startUpTime";

    @Expose
    private String robotId;
    @Expose
    private String clusterId;
    @Expose
    private Double inefficiencyRate;

    private int countInefficiencyComponents;

    private HashMap<Integer, Boolean> robotSignals = new HashMap<Integer, Boolean>();
    private int downTime;
    private Timestamp startUpTime;
    private Timestamp startDownTime;


    public Robot() {
    }

    public Robot(String robotId, String clusterId, int downTime) {
        this.robotId = robotId;
        this.clusterId = clusterId;
        this.downTime = downTime;
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
        downTime = document.getInteger(Robot.DOWN_TIME);
        startUpTime = new Timestamp(document.getLong(Robot.START_UP_TIME));
        startDownTime = new Timestamp(document.getLong(Robot.START_DOWN_TIME));
    }


    // When a src.test.Robot is initialized all the signals are set to true-
//    private void initRobotsSignals() {
//        TimeZone tz = TimeZone.getTimeZone("UTC");
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
//        df.setTimeZone(tz);
//        String nowAsISO = df.format(new Date());
//        for (int i = 0; i < 7; i++) {
//            robotSignals[i] = new Signal(i + 1, true, nowAsISO);
//        }
//    }

    public void setCountInefficiencyComponents(int countInefficiencyComponents) {
        this.countInefficiencyComponents = countInefficiencyComponents;
    }

    public void setDownTime(int downTime) {
        this.downTime = downTime;
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

    public int getDownTime() {
        return downTime;
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

    @Override
    public String toString() {
        return " {" +
                "\"robotId\": \"" + robotId + "\"" +
                ", \"inefficiencyRate\": " + inefficiencyRate +
                "}";
    }
}
