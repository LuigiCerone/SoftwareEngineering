package main.Controller;

import main.Model.Cluster;
import main.Model.DAO.ClusterDAO;
import main.Model.DAO.RobotDAO;
import main.Model.ReadData;
import main.Model.Robot;


public class ControllerSignals implements Runnable {
    private ReadData readData;

//    private HashMap<String, Zone> zones = null;

    public ControllerSignals(String readDataToDeserialize) {
        System.out.println(readDataToDeserialize);
        this.readData = new ReadData(readDataToDeserialize);
    }

    @Override
    public void run() {
        // TODO Write the following in a file.
        // new ReadDataDAO().insert(this.readData);
        ClusterDAO clusterDAO = new ClusterDAO();
        Cluster cluster = clusterDAO.findClusterByIdOrInsert(readData);

        RobotDAO robotDAO = new RobotDAO();
        Robot robot = robotDAO.findRobotByIdOrInsert(readData);

        // TODO Check if the signal value is already set.
//        new ControllerIR().updateComponentState(robot, robotDAO, readData, cluster, clusterDAO);
//        readData.getSignal();
//        readData.getValue();
    }

    public void work() {
        // TODO Write the following in a file.
        // new ReadDataDAO().insert(this.readData);
//        zones = new ZoneDAO().getZones();
//
//        if (zones.get(readData.getZone()) == null) {
//            // We need to add the zone, but for this we only need to add a new cluster with this zoneId.
//        } else {
//            // The zone is already present.
//            Zone workingZone = zones.get(readData.getZone());
//            if (workingZone.getCluster(readData.getCluster()) == null) {
//                // We need to add the cluster.
//                new ClusterDAO().insert(readData);
//            } else {
//                // The cluster is already present.
//                Cluster workingCluster = workingZone.getCluster(readData.getCluster());
//                if (workingCluster.getRobot(readData.getRobot()) == null) {
//                    // We need to add the robot.
//                    new RobotDAO().insert(readData);
//                } else {
//                    // The robot is already present, then update the count.
//                    new ControllerIR().updateComponentState(readData, workingCluster.getRobot(readData.getRobot()), workingCluster);
//                }
//            }


        Cluster workingCluster = new ClusterDAO().getCluster(readData);
        Robot workingRobot = new RobotDAO().getRobot(readData);
        new ControllerIR().updateComponentState(readData, workingRobot, workingCluster);
    }
}
