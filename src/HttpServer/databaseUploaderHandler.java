package HttpServer;

import Controller.ControllerSignals;
import Database.Database;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;

import org.apache.commons.io.IOUtils;

public class databaseUploaderHandler implements HttpHandler {
//    MongoDB mongoDB = new MongoDB();
    Database bs = new Database();

    public void handle(HttpExchange t) throws IOException {
        System.out.println("Just received a request.");
        if (t.getRequestMethod().equalsIgnoreCase("POST")) {
            InputStream is = t.getRequestBody();
            String value = IOUtils.toString(is, "UTF-8");
            insertIntoDB(value);
        }
        t.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
        t.close();
    }

    private void insertIntoDB(String data) {
//        mongoDB.getAllRobotsReadingsData();
        Thread myThread = new Thread(new ControllerSignals(data));
        myThread.start();
    }
}
