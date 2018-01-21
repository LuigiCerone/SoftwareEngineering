package main.HttpServer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import main.Controller.ControllerIR;
import main.Model.DAO.ZoneDAO;
import main.Model.Zone;

import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
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
            webSocket = new webSocket(getLocalLANAddress());
            webSocket.start();
        }
    }

    @Override
    public void run() {
        // TODO Replace Thread.sleep with TimerWatcher.
        System.out.println("I'm the DashboardIRThread and I'm now running!");
        //Update IR.
        HashMap<String, Zone> zones = null;
        try {
            new ControllerIR().calculateIR();
            zones = new ZoneDAO().getZones();
        } catch (Exception e) {
            e.printStackTrace();
        }
        webSocket.setData(gson.toJson(zones));
        webSocket.sendJSONToAll();

        // Data should be updated every 5mins = 300s = 300000ms;

        System.out.println("I'm the DashboardIRThread and I'm going to sleep!");
    }

    private String getLocalLANAddress() {
        try {
            Enumeration<NetworkInterface> b = NetworkInterface.getNetworkInterfaces();
            while (b.hasMoreElements()) {
                for (InterfaceAddress f : b.nextElement().getInterfaceAddresses())
                    if (f.getAddress().isSiteLocalAddress()) {
                        System.out.println(f.getAddress().getHostAddress());
                        return f.getAddress().getHostAddress();
                    }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

}