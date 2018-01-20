package main.Model;

import org.bson.Document;

public class Signal {
    //JSON field name.
    public static final String SIGNAL_NUMER = "number";
    public static final String SIGNAL_VALUE = "value";
    public static final String SIGNAL_TIMESTAMP = "timestamp";

    private int singalNumber;
    private boolean signalValue;
    private long timestamp;

    public Signal(int singalNumber, boolean signalValue, long timestamps) {
        this.singalNumber = singalNumber;
        this.signalValue = signalValue;
        this.timestamp = timestamps;
    }

    public Document toDocument() {
        return new Document()
                .append(SIGNAL_NUMER, singalNumber)
                .append(SIGNAL_VALUE, signalValue)
                .append(SIGNAL_TIMESTAMP, timestamp);
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

    public boolean getSignalValue() {
        return this.signalValue;
    }

    public long getTimestamps() {
        return timestamp;
    }

    public void setTimestamps(long timestamps) {
        this.timestamp = timestamps;
    }

}
