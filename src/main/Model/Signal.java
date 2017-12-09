package main.Model;

import java.sql.Timestamp;

public class Signal {
    //JSON field name.
    public static final String SIGNAL_NUMER = "signalNumber";
    public static final String SIGNAL_VALUE = "signalValue";
    public static final String SIGNAL_TIMESTAMP = "signalTimestamp";

    private int singalNumber;
    private boolean signalValue;
    private Timestamp timestamps;
    private String robotId;

    public Signal() {
    }

    public Signal(int singalNumber, boolean signalValue, Timestamp timestamps, String robotId) {
        this.singalNumber = singalNumber;
        this.signalValue = signalValue;
        this.timestamps = timestamps;
        this.robotId = robotId;
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

    public boolean getSignalValue() { return this.signalValue;
    }
    public Timestamp getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(Timestamp timestamps) {
        this.timestamps = timestamps;
    }

    @Override
    public String toString() {
        return "Signal{" +
                "singalNumber=" + singalNumber +
                ", signalValue=" + signalValue +
                ", timestamps='" + timestamps + '\'' +
                '}';
    }

    public String getRobotId() {
        return robotId;
    }
}
