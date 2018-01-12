package main.Controller;

import main.Main.Util;
import main.Model.Cluster;
import main.Model.DAO.ClusterDAO;
import main.Model.DAO.HistoryDAO;
import main.Model.DAO.RobotDAO;
import main.Model.DAO.SignalDAO;
import main.Model.ReadData;
import main.Model.Robot;
import main.Model.Signal;

import java.sql.Timestamp;

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
            historyDAO.insertPeriodEnd(robot.getRobotId(), readData.getTimestamp(), true);  // <--?
            // Start DOWN period.
            historyDAO.insertPeriodStart(robot.getRobotId(), readData.getTimestamp(), false);

            // The robot is now down.
            cluster.setCountInefficiencyComponents(countClusterInefficiencyComponents - 1);
            if (cluster.getCountInefficiencyComponents() == -1) {
                // The cluster is now down.
                clusterDAO.updateCountAndStartDown(cluster, readData);

                // End UP period.
                historyDAO.insertPeriodEnd(cluster.getClusterId(), readData.getTimestamp(), true); // <--?
                // Start DOWN period.
                historyDAO.insertPeriodStart(cluster.getClusterId(), readData.getTimestamp(), false);
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
            historyDAO.insertPeriodEnd(robot.getRobotId(), readData.getTimestamp(), false);
            // Start UP period.
            historyDAO.insertPeriodStart(robot.getRobotId(), readData.getTimestamp(), true);

            // The robot is now up.
            cluster.setCountInefficiencyComponents(countClusterInefficiencyComponents + 1);
            if (cluster.getCountInefficiencyComponents() == 0) {
                // The cluster is now up.
                downTimeDiffCluster = Util.differenceBetweenTimestamps(readData.getTimestamp(), cluster.getStartDownTime());
                clusterDAO.updateCountAndStopDown(cluster, readData, downTimeDiffCluster);

                // End DOWN period.
                historyDAO.insertPeriodEnd(cluster.getClusterId(), readData.getTimestamp(), false);
                // Start UP period.
                historyDAO.insertPeriodStart(cluster.getClusterId(), readData.getTimestamp(), true);
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

    public void calculateIR(RobotDAO robotDAO, ClusterDAO clusterDAO) {
        // TODO Chenge with the query on History.
        robotDAO.processRobotIR();
        clusterDAO.processClusterIR();
    }
}
