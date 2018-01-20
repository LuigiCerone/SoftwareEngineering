package main.Controller;

import main.Main.Util;
import main.Model.Cluster;
import main.Model.DAO.ClusterDAO;
import main.Model.DAO.HistoryDAO;
import main.Model.DAO.RobotDAO;
import main.Model.History;
import main.Model.ReadData;
import main.Model.Robot;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ControllerIR {


    public void updateComponentState(ReadData readData, Robot robot, Cluster cluster) {
        HistoryDAO historyDAO = new HistoryDAO();

        long downTimeDiffRobot = 0;
        long downTimeDiffCluster = 0;

        // Modify the robot counter stat.
        int countRobotInefficiencyComponents = robot.updateComponentState(readData.getValue());
//        System.out.println(countRobotInefficiencyComponents);

        int countClusterInefficiencyComponents = cluster.getCountInefficiencyComponents();

        // The robot has become down with this reading.
        if (countRobotInefficiencyComponents == -1 && robot.getStartDownTime() == null) { //  <--------- Puà darsi che era già down ed è risalito a -1.

            // Start counting for down time.
            // Save in the DB the timestamp in witch the robot has gone down, call this Y.
            // Notify the cluster that a robot is down by decrementing its count.
            new RobotDAO().updateCountAndStartDown(robot, readData);

            // Insert a log history for this downTime period start.
            // End UP period.
            historyDAO.insertPeriodEnd(robot.getRobotId(), readData.getTimestamp(), true, 0);  // <--?
            // Start DOWN period.
            historyDAO.insertPeriodStart(robot.getRobotId(), readData.getTimestamp(), false, 0);

            // The robot is now down.
            cluster.setCountInefficiencyComponents(countClusterInefficiencyComponents - 1);
            if (cluster.getCountInefficiencyComponents() == -1) {
                // The cluster is now down.
                new ClusterDAO().updateCountAndStartDown(cluster, readData);

                // End UP period.
                historyDAO.insertPeriodEnd(cluster.getClusterId(), readData.getTimestamp(), true, 1); // <--?
                // Start DOWN period.
                historyDAO.insertPeriodStart(cluster.getClusterId(), readData.getTimestamp(), false, 1);
            }
        }
        // The robot has become up with this reading.
        else if (countRobotInefficiencyComponents == 0) {
            // Stop counting for downtime, calculate it and add to robot.downTime.
            // Get Y and calculate the time.
            // Notify the cluster that a robot is up by incrementing its count.
            downTimeDiffRobot = Util.differenceBetweenTimestamps(readData.getTimestamp(), robot.getStartDownTime());
//            System.out.println(downTimeDiff + " con count : " + countInefficiencyComponents + "==" + robot.getCountInefficiencyComponents());

            new RobotDAO().updateCountAndStopDown(robot, readData, downTimeDiffRobot);

            // Update log history entry with time.
            // End DOWN period.
            historyDAO.insertPeriodEnd(robot.getRobotId(), readData.getTimestamp(), false, 0);
            // Start UP period.
            historyDAO.insertPeriodStart(robot.getRobotId(), readData.getTimestamp(), true, 0);

            // The robot is now up.
            cluster.setCountInefficiencyComponents(countClusterInefficiencyComponents + 1);
            if (cluster.getCountInefficiencyComponents() == 0) {
                // The cluster is now up.
                downTimeDiffCluster = Util.differenceBetweenTimestamps(readData.getTimestamp(), cluster.getStartDownTime());
                new ClusterDAO().updateCountAndStopDown(cluster, readData, downTimeDiffCluster);

                // End DOWN period.
                historyDAO.insertPeriodEnd(cluster.getClusterId(), readData.getTimestamp(), false, 1);
                // Start UP period.
                historyDAO.insertPeriodStart(cluster.getClusterId(), readData.getTimestamp(), true, 1);
            }
        } else {
            // The robot is still down/up.
            new RobotDAO().updateCount(robot);
        }

        // Modify the signal data in the DB.
        // TODO Add the following if needed.
//        Signal signal = new Signal(readData.getSignal(), readData.getValue(), readData.getTimestamp(), robot.getRobotId());
//        new SignalDAO().update(signal);
    }

    public void calculateIR() {
        HashMap<String, Double> clustersRates;
        HashMap<String, Double> robotsRates;

        Timestamp now = new Timestamp(System.currentTimeMillis());
        ZonedDateTime zonedDateTime = now.toInstant().atZone(ZoneId.of("UTC"));
        Timestamp oneHourAgo = Timestamp.from(zonedDateTime.minus(1, ChronoUnit.HOURS).toInstant());
        System.out.println("Timestamps are oneHourAgo: " + oneHourAgo.toString() + " now: " + now.toString());

        long nowLong = now.getTime();
        long oneHourAgoLong = oneHourAgo.getTime();

        HashSet<Cluster> clusters = new ClusterDAO().getAllClusters();
        clustersRates = calculateClustersIr(nowLong, oneHourAgoLong, clusters);
        if (clustersRates.size() > 0) {
            new ClusterDAO().updateIR(clustersRates);

            robotsRates = calculateRobotsIr(nowLong, oneHourAgoLong, clusters);
            new RobotDAO().updateIR(robotsRates);
        }
    }

    private double computeHistories(ArrayList<History> histories, long nowLong, long oneHourAgoLong) {
        long downTime = 0;
        long upTime = 0;
        for (History history : histories) {
            if (history.getEnd() == null) {
                long time = Util.differenceBetweenTimestamps(nowLong, history.getStart());
                if (time >= 3600) {
                    // This means the robot has been in this state for the last hour (and is still in this status).
                    if (history.getStatus())
                        upTime = 3600;
                    else
                        downTime = 3600;
                } else {
                    // This mean this is the currently status.
                    if (history.getStatus())
                        upTime += time;
                    else
                        downTime += time;
                }
            } else if (history.getStart() >= oneHourAgoLong && history.getEnd() <= nowLong) {
                // This means that both startTime and endTime are in last hour.
                long time = Util.differenceBetweenTimestamps(history.getEnd(), history.getStart());

                if (history.getStatus())
                    upTime += time;
                else
                    downTime += time;

                // Il caso in cui è iniziata più di un'ora fa ed ed è finita dopo Start ma prima di End.
            } else if (history.getStart() <= oneHourAgoLong) {
                // TODO Test this case.
                // This means that the entry is on the middle.
                long time = 3600 - downTime - upTime;
                if (history.getStatus())
                    upTime += time;
                else
                    downTime += time;
            }
        }

        float downTimeF;
        downTimeF = (float) downTime / 60; // downTime in minutes.

        // 60min = 1hour is the temporal window.
        double ir = ((double) downTimeF / 60) * 100;
        ir = (double) (Math.round(ir * 100.0) / 100.0);
        return ir;
    }

    HashMap<String, Double> calculateClustersIr(long nowLong, long oneHourAgoLong, HashSet<Cluster> clusters) {
        HashMap<String, Double> clustersRates = new HashMap<>();
        // Iterate over clusters and calculate ir.
        for (Cluster cluster : clusters) {
            ArrayList<History> histories = cluster.getHistories();
            clustersRates.put(cluster.getClusterId(), computeHistories(histories, nowLong, oneHourAgoLong));
        }
        return clustersRates;
    }

    HashMap<String, Double> calculateRobotsIr(long nowLong, long oneHourAgoLong, HashSet<Cluster> clusters) {
        HashMap<String, Double> robotsRates = new HashMap<>();
        // Iterate over clusters and calculate ir.
        for (Cluster cluster : clusters) {
            for (Robot robot : cluster.getRobotsList()) {
                ArrayList<History> histories = cluster.getHistories();
                robotsRates.put(robot.getRobotId(), computeHistories(histories, nowLong, oneHourAgoLong));
            }
        }
        return robotsRates;
    }
}
