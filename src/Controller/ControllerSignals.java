package Controller;

import Model.Cluster;
import Model.DAO.ClusterDAO;
import Model.DAO.RobotDAO;
import Model.ReadData;
import Model.DAO.ReadDataDAO;
import Model.Robot;


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

        Cluster cluster = new ClusterDAO().findClusterByIdOrInsert(readData);

        Robot robot = new RobotDAO().findRobotByIdOrInsert(readData);

        // TODO Modify the state of the robot according to the reading just received.
        new ControllerIRRobot().updateComponentState(robot, readData);
//        readData.getSignal();
//        readData.getValue();
    }
}
