package main.HttpServer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.Model.DAO.RobotDAO;
import main.Model.Robot;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

public class displayInefficiencyRateHandler implements HttpHandler {
    Gson gson;
    List<Robot> robotList;

    public displayInefficiencyRateHandler() {
        GsonBuilder b = new GsonBuilder();
        gson = b.excludeFieldsWithoutExposeAnnotation().create();
        robotList = new LinkedList<>();
        robotList.add(new Robot("1", "1", 1));
        robotList.add(new Robot("2", "2", 2));
        robotList.add(new Robot("3", "3", 3));
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
//        exchange.getRequestHeaders().set("Content-Type", String.format("application/json; charset=%s", StandardCharsets.UTF_8));
        getAllRobots();

        byte[] json = gson.toJson(robotList).getBytes();
//        System.out.println(json);

        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, json.length);

        OutputStream os = exchange.getResponseBody();
        os.write(json);
        os.close();

        exchange.close();
    }

    private void getAllRobots() {
        // Get all the information related to the robots here and update the robotList list.
        RobotDAO robotDAO = new RobotDAO();

        robotList = robotDAO.getAllRobots();
    }
}
