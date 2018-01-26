package main.HttpServer;

import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;

public class HttpServerInit {
//    private static ExecutorService pool = Executors.newFixedThreadPool(2);

    public HttpServerInit() throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(9000), 0);
        server.createContext("/robots", new databaseUploaderHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }

//    public static void addToThreadPool(Thread thread){
//        pool.execute(thread);
//    }
}