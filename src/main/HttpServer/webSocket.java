package main.HttpServer;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class webSocket extends WebSocketServer {
    HashSet<WebSocket> allClients;
    String serializedRobots = null;

    public webSocket() {
        super(new InetSocketAddress("192.168.0.4", 9091));
        allClients = new HashSet<WebSocket>();
    }

    @Override
    public void onOpen(WebSocket client, ClientHandshake handshake) {
        allClients.add(client);

        // TODO Change the following with robots stats.
        client.send("Welcome to the server!"); //This method sends a message to the new client
//        client.send(serializedRobots);

//        broadcast( "new connection: " + handshake.getResourceDescriptor() ); //This method sends a message to all clients connected
//        System.out.println("new connection to " + client.getRemoteSocketAddress());
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
        System.err.println("an error occured on connection " + conn.getRemoteSocketAddress() + ":" + ex);
    }

    @Override
    public void onStart() {
        System.out.println("server started successfully");
    }
}
