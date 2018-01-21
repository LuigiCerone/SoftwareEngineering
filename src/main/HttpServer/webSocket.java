package main.HttpServer;

import main.Main.Main;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.HashSet;

public class webSocket extends WebSocketServer {
    private HashSet<WebSocket> allClients;
    private String serializedData = null;

    public webSocket(String IP) {
        super(new InetSocketAddress(IP, 9999));
        allClients = new HashSet<WebSocket>();
        Main.setIP(IP);
    }


    public void setData(String serializedData) {
        this.serializedData = serializedData;
        System.out.println("Set!");
    }

    public boolean isSomeoneConnected() {
        if (allClients.size() > 0)
            return true;
        else return false;
    }

    @Override
    public void onOpen(WebSocket client, ClientHandshake handshake) {
//        System.out.println("Address: " + client.getRemoteSocketAddress().getAddress().getHostAddress());
//        System.out.println("Port: " + client.getRemoteSocketAddress().getPort());
        allClients.add(client);
        if (serializedData != null)
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
        ex.printStackTrace();
        if (conn.getRemoteSocketAddress() != null)
            System.err.println("an error occured on connection " + conn.getRemoteSocketAddress() + ":" + ex.getMessage());
    }

    @Override
    public void onStart() {
        System.out.println("server started successfully");
    }

    // Update stats.
    public void sendJSONToAll() {
        for (WebSocket client : allClients)
            client.send(serializedData);
    }

    private void sendJSONToNew(WebSocket client) {
        client.send(serializedData);
    }

}
