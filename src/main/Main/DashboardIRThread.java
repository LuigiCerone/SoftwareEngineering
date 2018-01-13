package main.Main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import main.Controller.ControllerIR;
import main.HttpServer.webSocket;
import main.Model.Cluster;
import main.Model.DAO.ClusterDAO;
import main.Model.DAO.RobotDAO;
import main.Model.Robot;
import org.json.JSONObject;

import java.util.LinkedList;

public class DashboardIRThread implements Runnable {
    webSocket webSocket = null;
    Gson gson;

    public DashboardIRThread() {
        GsonBuilder b = new GsonBuilder();
        gson = b.excludeFieldsWithoutExposeAnnotation().create();
    }

    @Override
    public void run() {
        // Update IR.
        new ControllerIR().calculateIR();

        LinkedList<Robot> allRobots = new LinkedList<>();
        allRobots = getAllRobots();

        LinkedList<Cluster> allClusters = new LinkedList<>();
        allClusters = getAllClusters();

        JSONObject jsonObject = new JSONObject();

        String robots = gson.toJson(allRobots);
        jsonObject.put("robots", robots);

        String clusters = gson.toJson(allClusters);
        jsonObject.put("clusters", clusters);


//        client.send(json); //This method sends a message to the new client

        // Start the webSocket.
        if (webSocket == null) {
            webSocket = new webSocket();
            webSocket.start();
        }

        webSocket.setData(jsonObject.toString());

        // Data should be updated every 5mins = 300s = 300000ms;
        try {
            Thread.sleep(300000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private LinkedList<Cluster> getAllClusters() {
        return (LinkedList<Cluster>) new ClusterDAO().getAllClusters();
    }

    private LinkedList<Robot> getAllRobots() {
        // Get all the information related to the robots here and update the robotList list.
        return (LinkedList<Robot>) new RobotDAO().getAllRobots();
    }
}
