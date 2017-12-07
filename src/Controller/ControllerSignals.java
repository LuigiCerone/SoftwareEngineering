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

        // TODO Check if the cluster exists otherwise create it.
        Cluster cluster = new ClusterDAO().findClusterByIdOrInsert(readData);
        // TODO Check if the robot exists otherwise create it.
        Robot robot = new RobotDAO().findRobotByIdOrInsert(readData);
//        mongoDB.insertReadingsData(readData);
//        mongoDB.insertRobotInCluster(readData);
    }
}
