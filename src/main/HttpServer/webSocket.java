package main.HttpServer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import main.Model.DAO.RobotDAO;
import main.Model.Robot;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.LinkedList;

public class webSocket extends WebSocketServer {
    HashSet<WebSocket> allClients;
    LinkedList<Robot> robotList;
    String serializedRobots = null;
    String serializedClusters = null;

    public webSocket() {
        super(new InetSocketAddress("127.0.0.1", 9091));
        allClients = new HashSet<WebSocket>();
        System.out.println("Constructor");
    }

    public void setRobot(String serializedRobots) { this.serializedRobots = serializedRobots; }
    public void setCluster(String serializedClusters) { this.serializedClusters = serializedClusters; }


    @Override
    public void onOpen(WebSocket client, ClientHandshake handshake) {
        allClients.add(client);
        sendJSONToNew(client);
    }

    // Called when a client disconnects.
    @Override
    public void onClose(WebSocket client, int code, String reason, boolean remote) {
        System.out.println("closed " + client.getRemoteSocketAddress() + " with exit code " + code + " additional info: " + reason);
        allClients.remove(client);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("received message from " + conn.getRemoteSocketAddress() + ": " + message);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
//        System.err.println("an error occured on connection " + conn.getRemoteSocketAddress() + ":" + ex.getMessage());
    }

    @Override
    public void onStart() {
        System.out.println("server started successfully");
    }

    // Update stats.
    private void sendJSONToAll() {
        for(WebSocket client : allClients)
            client.send(serializedRobots.toString());
    }

    private void sendJSONToNew(WebSocket client) {
        client.send(serializedRobots);
    }

}
