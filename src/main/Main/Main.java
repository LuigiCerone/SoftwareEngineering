package main.Main;

import main.HttpServer.HttpServerInit;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Running mode");

        new HttpServerInit();

        ScheduledExecutorService scheduler =
                Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new DashboardIRThread(), 0, 5, TimeUnit.MINUTES);
        scheduler.scheduleAtFixedRate(new OldHistoryCleanerThread(), 30, 90, TimeUnit.MINUTES);

    }
}
