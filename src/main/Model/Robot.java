package main.Model;

import java.sql.Timestamp;
import java.util.Arrays;

public class Robot {
    //JSON field names.
    public static final String ROBOT_ID = "robotId";
    public static final String CLUSTER_ID = "clusterId";
    public static final String INEFFICIENCY_RATE = "ir";
    public static final String DOWN_TIME = "downTime";
    public static final String COUNT_INEFFICIENCY_COMPONENTS = "count";
    public static final String  START_DOWN_TIME = "startDownTime";
    public static final String  START_UP_TIME = "startUpTime";

    private String robotId;
    private String clusterId;
    private float inefficiencyRate;
    private int countInefficiencyComponents;
    private Signal[] robotSignals = new Signal[7];
    private int downTime;
    private Timestamp startUpTime;
    private Timestamp startDownTime;


    public Robot() {
    }

    public Robot(String robotId, String clusterId, int downTime, Signal[] signals) {
        this.robotId = robotId;
        this.clusterId = clusterId;
        this.robotSignals = signals;
        this.downTime = downTime;
    }


    // When a Robot is initialized all the signals are set to true-
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

    public void setRobotSignals(Signal[] robotSignals) {
        this.robotSignals = robotSignals;
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

    public float getInefficiencyRate() {
        return inefficiencyRate;
    }

    public void setInefficiencyRate(float inefficiencyRate) {
        this.inefficiencyRate = inefficiencyRate;
    }

    public Signal[] getRobotSignals() {
        return robotSignals;
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

    @Override
    public String toString() {
        return "Robot{" +
                "robotId='" + robotId + '\'' +
                ", clusterId='" + clusterId + '\'' +
                ", inefficiencyRate=" + inefficiencyRate +
                ", robotSignals=" + Arrays.toString(robotSignals) +
                '}';
    }

    public int updateComponentState(boolean value) {
        // The reading contains a down signal from this robot.
        if(!value){
            this.countInefficiencyComponents--;
            System.out.println("A component of this robot is not working.");
        } else if(value){
            this.countInefficiencyComponents++;
            System.out.println("A component of this robot is working.");
        }else{
            System.out.println("Something strange has just happened.");
        }
        return this.countInefficiencyComponents;
    }
}
