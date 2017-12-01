package Model;

import java.util.Date;

public class Signal {
    private int id;
    private int singalNumber;
    private boolean signalValue;
    private String robotId;
    private Date timestamps;

    public Signal(int id, int singalNumber, boolean signalValue, String robotId, Date timestamps) {
        this.id = id;
        this.singalNumber = singalNumber;
        this.signalValue = signalValue;
        this.robotId = robotId;
        this.timestamps = timestamps;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSingalNumber() {
        return singalNumber;
    }

    public void setSingalNumber(int singalNumber) {
        this.singalNumber = singalNumber;
    }

    public boolean isSignalValue() {
        return signalValue;
    }

    public void setSignalValue(boolean signalValue) {
        this.signalValue = signalValue;
    }

    public String getRobotId() {
        return robotId;
    }

    public void setRobotId(String robotId) {
        this.robotId = robotId;
    }

    public Date getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(Date timestamps) {
        this.timestamps = timestamps;
    }
}
