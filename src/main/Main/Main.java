package main.Main;

import main.HttpServer.HttpServerInit;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Running mode");

//        MongoDB mdb = new MongoDB();
//        mdb.getAllRobotsData();

        HttpServerInit serverInit = new HttpServerInit();
    }
}
