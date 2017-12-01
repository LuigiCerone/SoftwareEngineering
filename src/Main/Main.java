package Main;

import Database.MongoDB;
import HttpServer.HttpServerInit;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("test");

//        MongoDB mdb = new MongoDB();
//        mdb.getAllRobotsData();

        HttpServerInit serverInit = new HttpServerInit();
    }
}
