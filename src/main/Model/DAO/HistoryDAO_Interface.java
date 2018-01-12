package main.Model.DAO;

import java.sql.Timestamp;

public interface HistoryDAO_Interface {
    public void insertPeriodStart(String deviceId, Timestamp start, boolean status);

    public void insertPeriodEnd(String deviceId, Timestamp end, boolean status);
}
