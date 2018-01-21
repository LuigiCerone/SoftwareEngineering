package main.Controller;

import main.Model.Cluster;
import main.Model.DAO.ClusterDAO;
import main.Model.DAO.RobotDAO;
import main.Model.ReadData;
import main.Model.Robot;
import main.Model.Signal;
import org.bson.Document;

import java.util.ArrayList;


public class ControllerSignals implements Runnable {
    private ReadData readData;

//    private HashMap<String, Zone> zones = null;

    public ControllerSignals(String readDataToDeserialize) {
        // Uncomment the following if you want to see the read data.
//        System.out.println(readDataToDeserialize);
        this.readData = new ReadData(readDataToDeserialize);
    }

    public ControllerSignals() {

    }

    @Override
    public void run() {
        Cluster workingCluster = new ClusterDAO().getCluster(readData);
        Robot workingRobot = new RobotDAO().getRobot(readData);
        new ControllerIR().updateComponentState(readData, workingRobot, workingCluster);
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

    public ArrayList<Document> createSignals(long timestamp) {
        int SIGNALS_NUMBER = 8;
        ArrayList<Document> signalsAsDoc = new ArrayList<>(SIGNALS_NUMBER);
        for (int i = 1; i < SIGNALS_NUMBER; i++) {
            signalsAsDoc.add(new Signal(i, true, timestamp).toDocument());
        }
        return signalsAsDoc;
    }
}
