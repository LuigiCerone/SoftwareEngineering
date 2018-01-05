package main.HttpServer;

import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServerInit {
    private static ExecutorService pool = Executors.newFixedThreadPool(16);

    public HttpServerInit() throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(9000), 0);
        server.createContext("/robots", new databaseUploaderHandler());
        server.createContext("/ir", new inefficiencyRateCalculatorHandler());
        server.createContext("/display", new displayInefficiencyRateHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    public static void addToThreadPool(Thread thread){
        pool.execute(thread);
    }
}