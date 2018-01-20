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
        // Uncomment the following if you want to see the read data.
//        System.out.println(readDataToDeserialize);
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
    }

    public void work() {
        Cluster workingCluster = new ClusterDAO().getCluster(readData);
        Robot workingRobot = new RobotDAO().getRobot(readData);
        try {
            new ControllerIR().updateComponentState(readData, workingRobot, workingCluster);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
