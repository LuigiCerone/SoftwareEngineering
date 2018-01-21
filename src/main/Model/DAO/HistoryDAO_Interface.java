package main.Model.DAO;

import java.sql.Timestamp;

public interface HistoryDAO_Interface {

    void insertPeriodStart(String deviceId, Timestamp start, boolean status, int type);

    // type=0 means src.test.Robot, type=1 means Cluster.
    void insertPeriodEnd(String deviceId, Timestamp end, boolean status, int type);

    void oldHistoriesCleaner();
}
