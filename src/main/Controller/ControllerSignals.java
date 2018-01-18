package main.Controller;

import main.Model.Cluster;
import main.Model.DAO.ClusterDAO;
import main.Model.DAO.RobotDAO;
import main.Model.ReadData;
import main.Model.Robot;


public class ControllerSignals implements Runnable {
    private String readDataToDeserialize;
    private ReadData readData;

    public ControllerSignals(String readDataToDeserialize) {
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

//        HashMap<Integer, Boolean> robotSignals = null;
//            // Search these data in the DB.
//        robotSignals = new SignalDAO().getAllSignalsForRobot(robot.getRobotId());
//        robot.setRobotSignals(robotSignals);

        // TODO Check if the signal value is already set.
        new ControllerIR().updateComponentState(robot, robotDAO, readData, cluster, clusterDAO);
//        readData.getSignal();
//        readData.getValue();
    }

    public void work() {
        // TODO Write the following in a file.
        // new ReadDataDAO().insert(this.readData);
        ClusterDAO clusterDAO = new ClusterDAO();
//        Cluster cluster = clusterDAO.findClusterByIdOrInsert(readData);
        Cluster cluster = clusterDAO.callProcedure(readData);
        RobotDAO robotDAO = new RobotDAO();
//        Robot robot = robotDAO.findRobotByIdOrInsert(readData);
        Robot robot = robotDAO.callProcedure(readData);

//        HashMap<Integer, Boolean> robotSignals = null;
//            // Search these data in the DB.
//        robotSignals = new SignalDAO().getAllSignalsForRobot(robot.getRobotId());
//        robot.setRobotSignals(robotSignals);

        // TODO Check if the signal value is already set.
        new ControllerIR().updateComponentState(robot, robotDAO, readData, cluster, clusterDAO);
//        readData.getSignal();
//        readData.getValue();
    }
}
