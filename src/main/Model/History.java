package main.Model;

import java.sql.Timestamp;
import java.util.Comparator;

public class History {
    //JSON and DatabaseConnector field names.
    public static final String HISTORY_ID = "id";
    public static final String DEVICE_ID = "deviceId";
    public static final String START = "startTime";
    public static final String END = "endTime";
    public static final String STATUS = "status";

    private int id;
    private String deviceId;
    private Timestamp start;
    private Timestamp end;
    private boolean status; // If was 1 (=UP) or 0 (=DOWN)

    public History(int id, String deviceId, Timestamp start, Timestamp end, boolean status) {
        this.id = id;
        this.deviceId = deviceId;
        this.start = start;
        this.end = end;
        this.status = status;
    }

    public History() {
    }

    ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }


    public static class HistoryComparator implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            History a = (History) o1;
            History b = (History) o2;

            if (a.getId() == b.getId()) return 0;

            if (a.getId() < b.getId()) {
                return 1;  //do the other way
            }
            return -1;
        }
    }


    @Override
    public boolean equals(Object obj) {
        return id == ((History) obj).getId();
    }
}
