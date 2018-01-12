package main.Model;

import java.sql.Timestamp;

public class History {
    private String id;
    private String deviceId;
    private Timestamp start;
    private Timestamp end;
    private boolean status; // If was 1 (=UP) or 0 (=DOWN)

    public History(String id, String deviceId, Timestamp start, Timestamp end, boolean status) {
        this.id = id;
        this.deviceId = deviceId;
        this.start = start;
        this.end = end;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Timestamp getStart() {
        return start;
    }

    public void setStart(Timestamp start) {
        this.start = start;
    }

    public Timestamp getEnd() {
        return end;
    }

    public void setEnd(Timestamp end) {
        this.end = end;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
