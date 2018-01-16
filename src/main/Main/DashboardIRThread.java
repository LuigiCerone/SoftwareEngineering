package main.Main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import main.Controller.ControllerIR;
import main.HttpServer.webSocket;
import main.Model.Cluster;
import main.Model.DAO.ClusterDAO;
import main.Model.DAO.RobotDAO;
import main.Model.DAO.ZoneDAO;
import main.Model.Zone;

import java.util.HashMap;
import java.util.LinkedList;

public class DashboardIRThread implements Runnable {
    webSocket webSocket = null;
    Gson gson;

    public DashboardIRThread() {
        GsonBuilder b = new GsonBuilder();
        gson = b
                .excludeFieldsWithoutExposeAnnotation()
                .enableComplexMapKeySerialization()
                .create();


//        WebSocketImpl.DEBUG = true;
        // Start the webSocket.
        if (webSocket == null) {
            webSocket = new webSocket();
            webSocket.start();
        }
    }

    @Override
    public void run() {

        while (true) {
//            if (webSocket.isSomeoneConnected()) {
            System.out.println("I'm the DashboardIRThread and I'm now running!");
            // Update IR.
            new ControllerIR().calculateIR();

            HashMap<String, Cluster> clusters = new ClusterDAO().getAllClusterMap();
            new RobotDAO().populateWithRobots(clusters);
            HashMap<String, Zone> zones = new ZoneDAO().populateWithZones(clusters);

//            LinkedList<Robot> allRobots = (LinkedList<Robot>) new RobotDAO().getAllRobots();
//
//            LinkedList<Cluster> allClusters = new LinkedList<>();
//            allClusters = getAllClusters();
//
//            JSONObject jsonObject = new JSONObject();
//
//            String robots = gson.toJson(allRobots);
//            jsonObject.put("robots", robots);

//            String clusters = gson.toJson(allClusters);
//            jsonObject.put("clusters", clusters);
//
//            HashMap<String, Robot> map = new HashMap<>();

//            webSocket.setData(jsonObject.toString());

            webSocket.setData(gson.toJson(zones));
            webSocket.sendJSONToAll();
//            }

            // Data should be updated every 5mins = 300s = 300000ms;
            try {
                System.out.println("I'm the DashboardIRThread and I'm going to sleep!");
                Thread.sleep(120000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private LinkedList<Cluster> getAllClusters() {
        return (LinkedList<Cluster>) new ClusterDAO().getAllClusters();
    }
}
