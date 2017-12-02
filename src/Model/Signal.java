package Model;

import java.util.Date;

public class Signal {
    private int singalNumber;
    private boolean signalValue;
    private String timestamps;

    public Signal(int singalNumber, boolean signalValue, String timestamps) {
        this.singalNumber = singalNumber;
        this.signalValue = signalValue;
        this.timestamps = timestamps;
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

    public String getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(String timestamps) {
        this.timestamps = timestamps;
    }
}
