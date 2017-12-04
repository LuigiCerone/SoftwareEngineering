package Model;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.Date;

public class Signal {
    //JSON field name.
    public static final String SIGNAL_NUMER = "signalNumber";
    public static final String SIGNAL_VALUE = "signalValue";
    public static final String SIGNAL_TIMESTAMP = "signalTimestamp";

    private int singalNumber;
    private boolean signalValue;
    private String timestamps;

    public Signal() {
    }

    @BsonCreator
    public Signal(@BsonProperty(SIGNAL_NUMER) int singalNumber,
                  @BsonProperty(SIGNAL_VALUE) boolean signalValue,
                  @BsonProperty(SIGNAL_TIMESTAMP) String timestamps) {
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

    @Override
    public String toString() {
        return "Signal{" +
                "singalNumber=" + singalNumber +
                ", signalValue=" + signalValue +
                ", timestamps='" + timestamps + '\'' +
                '}';
    }
}
