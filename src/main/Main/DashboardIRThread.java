package main.Main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import main.Controller.ControllerIR;
import main.HttpServer.webSocket;
import main.Model.DAO.ZoneDAO;
import main.Model.Zone;

import java.util.HashMap;

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
        // TODO Replace Thread.sleep with TimerWatcher.
        while (true) {
            if (webSocket.isSomeoneConnected()) {
                System.out.println("I'm the DashboardIRThread and I'm now running!");
                //Update IR.
                new ControllerIR().calculateIR();
                HashMap<String, Zone> zones = new ZoneDAO().getZones();
                webSocket.setData(gson.toJson(zones));
                webSocket.sendJSONToAll();

                // Data should be updated every 5mins = 300s = 300000ms;
                try {
                    System.out.println("I'm the DashboardIRThread and I'm going to sleep!");
                    Thread.sleep(120000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}