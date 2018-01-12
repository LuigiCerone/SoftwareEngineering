package main.Model.DAO;

import main.Model.History;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedList;

public interface HistoryDAO_Interface {

    void insertPeriodStart(String deviceId, Timestamp start, boolean status, int type);

    // type=0 means Robot, type=1 means Cluster.
    void insertPeriodEnd(String deviceId, Timestamp end, boolean status, int type);

    HashMap<String, LinkedList<History>> processIR(Timestamp now, Timestamp oneHourAgo, int type);

}
