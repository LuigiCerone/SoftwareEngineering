package main.HttpServer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.Controller.ControllerSignals;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class databaseUploaderHandler implements HttpHandler {
    String value = null;

    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            InputStream is = exchange.getRequestBody();
            value = IOUtils.toString(is, "UTF-8");
            System.out.println("Just received a request: " + value);
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            exchange.close();

            insertIntoDB(value);
        }
    }

    private void insertIntoDB(String data) {
        // Add the thread to the threads pool.
//        Thread myThread = new Thread(new ControllerSignals(data));
//        HttpServerInit.addToThreadPool(myThread);
        try {
            new ControllerSignals(data).work();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
