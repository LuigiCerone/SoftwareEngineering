package main.Model;

import org.bson.Document;

import java.sql.Timestamp;

public class History {
    //JSON and DatabaseConnector field names.
    public static final String DEVICE_ID = "deviceId";
    public static final String START = "startTime";
    public static final String END = "endTime";
    public static final String STATUS = "status";

    private String deviceId;
    private Long start;
    private Long end;
    private boolean status; // If was 1 (=UP) or 0 (=DOWN)

    // If type == 0 is a Robot, else if type == 1 is a Cluster.
    public History(ReadData readData, Timestamp startUp, int type) {
        if (type == 0)
            this.deviceId = readData.getRobot();
        else if (type == 1)
            this.deviceId = readData.getCluster();
        this.start = startUp.getTime();
        this.end = null;
        this.status = true;
    }

    public History(String deviceId, Timestamp start, boolean status) {
        this.deviceId = deviceId;
        this.start = start.getTime();
        this.end = null;
        this.status = status;
    }

    public History(Document document) {
        this.deviceId = document.getString(History.DEVICE_ID);
        this.start = document.getLong(History.START);
        this.end = document.getLong(History.END);
        this.status = document.getBoolean(History.STATUS);
    }

    public Document toDocument() {
        return new Document()
                .append(History.DEVICE_ID, deviceId)
                .append(History.START, start)
                .append(History.END, end)
                .append(History.STATUS, status);
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public long getStart() {
        return start;
    }

    public void setStart(Timestamp start) {
        this.start = start.getTime();
    }

    public Long getEnd() {
        return end;
    }

    public void setEnd(Timestamp end) {
        this.end = end.getTime();
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

}
