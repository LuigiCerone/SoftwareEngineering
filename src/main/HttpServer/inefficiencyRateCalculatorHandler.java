package main.HttpServer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.Controller.ControllerIR;
import main.Model.DAO.ClusterDAO;
import main.Model.DAO.RobotDAO;

import java.io.IOException;
import java.net.HttpURLConnection;

public class inefficiencyRateCalculatorHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Just received a request.");
        calculateIR();
        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
        exchange.close();
    }

    private void calculateIR() {
        new ControllerIR().calculateIR();
    }
}
