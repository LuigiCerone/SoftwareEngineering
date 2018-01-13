package main.HttpServer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class dashboardHandler implements HttpHandler {
    webSocket webSocket;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (webSocket == null) {
            webSocket = new webSocket();
        }

        webSocket.run();
    }
}
