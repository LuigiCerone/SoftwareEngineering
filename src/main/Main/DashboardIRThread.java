package main.Main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import main.Controller.ControllerIR;
import main.HttpServer.webSocket;
import main.Model.DAO.RobotDAO;
import main.Model.Robot;

import java.util.LinkedList;

public class DashboardIRThread implements Runnable {
    webSocket webSocket = null;
    Gson gson;

    public DashboardIRThread(){
        GsonBuilder b = new GsonBuilder();
        gson = b.excludeFieldsWithoutExposeAnnotation().create();
    }

    @Override
    public void run() {
        // Update IR.
        new ControllerIR().calculateIR();

        LinkedList<Robot> allRobots = new LinkedList<>();
        allRobots = getAllRobots();

        // TODO Add clusters.

        String serializedRobots = gson.toJson(allRobots);
//        client.send(json); //This method sends a message to the new client
        webSocket.setRobot(serializedRobots);

        // Start the webSocket.
        if (webSocket == null) {
            webSocket = new webSocket();
            webSocket.start();
        }

        // Data should be updated every 5mins = 300s = 300000ms;
        try {
            Thread.sleep(300000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private LinkedList<Robot> getAllRobots() {
        // Get all the information related to the robots here and update the robotList list.
        RobotDAO robotDAO = new RobotDAO();
        return (LinkedList<Robot>) robotDAO.getAllRobots();
    }
}
