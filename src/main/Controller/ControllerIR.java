package main.Controller;

import main.Main.Util;
import main.Model.*;
import main.Model.DAO.ClusterDAO;
import main.Model.DAO.HistoryDAO;
import main.Model.DAO.RobotDAO;
import main.Model.DAO.SignalDAO;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ControllerIR {


    public void updateComponentState(Robot robot, RobotDAO robotDAO, ReadData readData, Cluster cluster, ClusterDAO clusterDAO) {
        HistoryDAO historyDAO = new HistoryDAO();

        long downTimeDiffRobot = 0;
        long downTimeDiffCluster = 0;

        // Check if the stored signal is the same of the just received one.
        if (robot.getRobotSignal(readData.getSignal()) == readData.getValue()) {
            return;
        }


        // Modify the robot counter stat.
        int countRobotInefficiencyComponents = robot.updateComponentState(readData.getValue());
        System.out.println(countRobotInefficiencyComponents);

        int countClusterInefficiencyComponents = cluster.getCountInefficiencyComponents();

        // The robot has become down with this reading.
        if (countRobotInefficiencyComponents == -1 && robot.getStartDownTime() == null) { //  <--------- Puà darsi che era già down ed è risalito a -1.

            // Start counting for down time.
            // Save in the DB the timestamp in witch the robot has gone down, call this Y.
            // Notify the cluster that a robot is down by decrementing its count.
            robotDAO.updateCountAndStartDown(robot, readData);

            // Insert a log history for this downTime period start.
            // End UP period.
            historyDAO.insertPeriodEnd(robot.getRobotId(), readData.getTimestamp(), true, 0);  // <--?
            // Start DOWN period.
            historyDAO.insertPeriodStart(robot.getRobotId(), readData.getTimestamp(), false, 0);

            // The robot is now down.
            cluster.setCountInefficiencyComponents(countClusterInefficiencyComponents - 1);
            if (cluster.getCountInefficiencyComponents() == -1) {
                // The cluster is now down.
                clusterDAO.updateCountAndStartDown(cluster, readData);

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

            robotDAO.updateCountAndStopDown(robot, readData, downTimeDiffRobot);

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
                clusterDAO.updateCountAndStopDown(cluster, readData, downTimeDiffCluster);

                // End DOWN period.
                historyDAO.insertPeriodEnd(cluster.getClusterId(), readData.getTimestamp(), false, 1);
                // Start UP period.
                historyDAO.insertPeriodStart(cluster.getClusterId(), readData.getTimestamp(), true, 1);
            }

        } else {
            // The robot is still down/up.
            robotDAO.updateCount(robot);
            // TODO is the following required? Nope.
            // clusterDAO.updateCount(cluster);
        }

        // Modify the signal data in the DB.
        Signal signal = new Signal(readData.getSignal(), readData.getValue(), readData.getTimestamp(), robot.getRobotId());
        new SignalDAO().update(signal);
    }

    public void calculateIR() {
        // TODO Change with the query on History.
        HistoryDAO historyDAO = new HistoryDAO();

        Timestamp now = new Timestamp(System.currentTimeMillis());
        ZonedDateTime zonedDateTime = now.toInstant().atZone(ZoneId.of("UTC"));
        Timestamp oneHourAgo = Timestamp.from(zonedDateTime.minus(1, ChronoUnit.HOURS).toInstant());
        System.out.println("Timestamps are oneHourAgo: " + oneHourAgo.toString() + " now: " + now.toString());

        // Robots.
        HashMap<String, LinkedList<History>> robots = historyDAO.processIR(now, oneHourAgo, 0);
        HashMap<String, Float> robotsIR = computeIR(robots, now, oneHourAgo, 0);
        new RobotDAO().updateIR(robotsIR);

        // Clusters.
        HashMap<String, LinkedList<History>> clusters = historyDAO.processIR(now, oneHourAgo, 1);
        HashMap<String, Float> clustersIR = computeIR(clusters, now, oneHourAgo, 1);
        new ClusterDAO().updateIR(clustersIR);
//        robotDAO.processRobotIR();
//        clusterDAO.processClusterIR();
    }

    private HashMap<String, Float> computeIR(HashMap<String, LinkedList<History>> map, Timestamp now, Timestamp oneHourAgo, int type) {
        HashMap<String, Float> rates = new HashMap<>();

        for (Map.Entry<String, LinkedList<History>> entry : map.entrySet()) {
            LinkedList<History> histories = entry.getValue();
            String deviceId = entry.getKey();

            long upTime = 0;
            long downTime = 0;

            Collections.sort(histories, new History.HistoryComparator()); // Descending order based on history.ID .
            for (History history : histories) {
                if (history.getEnd() == null) {
                    long time = Util.differenceBetweenTimestamps(now, history.getStart());
                    if (time >= 3600) {
                        // This means the robot has been in this state for the last hour (and is still in this status).
                        if (history.getStatus())
                            upTime = 3600;
                        else downTime = 3600;
                    } else {
                        // This mean this is the currently status.
                        if (history.getStatus())
                            upTime += time;
                        else
                            downTime += time;
                    }
                } else if (history.getStart().after(oneHourAgo) && history.getEnd().before(now)) {
                    // This means that both startTime and endTime are in last hour.
                    long time = Util.differenceBetweenTimestamps(history.getEnd(), history.getStart());

                    if (history.getStatus())
                        upTime += time;
                    else
                        downTime += time;
                } else if (history.getStart().before(oneHourAgo)) {
                    // This means that the entry is on the middle.
                    long time = 3600 - downTime - upTime;
                    if (history.getStatus())
                        upTime += time;
                    else
                        downTime += time;
                }
            }

            float downTimeF;
            downTimeF = (float)downTime / 60; // downTime in minutes.

            // 60 is the temporal window.
            float ir = ((float) downTimeF / 60) * 100;
            ir = (float) (Math.round(ir * 100.0) / 100.0);
            rates.put(deviceId, ir);
        }
        return rates;
    }
}
