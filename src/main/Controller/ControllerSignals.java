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

        // TODO Modify the state of the robot according to the reading just received.
        new ControllerIRRobot().updateComponentState(robot, readData, robotDAO);
//        readData.getSignal();
//        readData.getValue();
    }
}
