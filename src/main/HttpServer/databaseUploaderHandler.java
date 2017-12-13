package main.HttpServer;

import main.Controller.ControllerSignals;
import main.Database.Database;
import main.HttpServer.HttpServerInit;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;

import org.apache.commons.io.IOUtils;

public class databaseUploaderHandler implements HttpHandler {

    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Just received a request.");
        if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            InputStream is = exchange.getRequestBody();
            String value = IOUtils.toString(is, "UTF-8");
            insertIntoDB(value);
        }
        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
        exchange.close();
    }

    private void insertIntoDB(String data) {
        // Add the thread to the threads pool.

        Thread myThread = new Thread(new ControllerSignals(data));
        HttpServerInit.addToThreadPool(myThread);
    }
}
