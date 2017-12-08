package Model;

public class Signal {
    //JSON field name.
    public static final String SIGNAL_NUMER = "signalNumber";
    public static final String SIGNAL_VALUE = "signalValue";
    public static final String SIGNAL_TIMESTAMP = "signalTimestamp";

    private int singalNumber;
    private boolean signalValue;
    private String timestamps;
    private String robotId;

    public Signal() {
    }

    public Signal(int singalNumber, boolean signalValue, String timestamps, String robotId) {
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
    public String getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(String timestamps) {
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
