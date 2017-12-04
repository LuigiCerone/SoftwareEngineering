package Model;

import eu.dozd.mongo.annotation.Entity;
import org.bson.*;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.conversions.Bson;
@Entity
public class ReadData {
    // JSON field name.
    public static final String ROBOT = "robot";
    public static final String CLUSTER = "cluster";
    public static final String ZONE = "zone";
    public static final String SIGNAL = "signal";
    public static final String VALUE = "value";
    public static final String TIMESTAMP = "timestamp";

    String robot;
    String cluster;
    String zone;
    Integer signal;
    Boolean value;
    String timestamp;

    public ReadData(){
    }

    @BsonCreator
    public ReadData(@BsonProperty(ROBOT) String robot,
                    @BsonProperty(CLUSTER) String cluster,
                    @BsonProperty(ZONE) String zone,
                    @BsonProperty(SIGNAL) Integer signal,
                    @BsonProperty(VALUE) Boolean value,
                    @BsonProperty(TIMESTAMP) String timestamp) {
        this.robot = robot;
        this.cluster = cluster;
        this.zone = zone;
        this.signal = signal;
        this.value = value;
        this.timestamp = timestamp;
    }


    public String getRobot() {
        return robot;
    }

    public String getCluster() {
        return cluster;
    }

    public String getZone() {
        return zone;
    }

    public int getSignal() {
        return signal;
    }

    public boolean getValue() {
        return value;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setRobot(String robot) {
        this.robot = robot;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public void setSignal(int signal) {
        this.signal = signal;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ReadData{" +
                "robot='" + robot + '\'' +
                ", cluster='" + cluster + '\'' +
                ", zone='" + zone + '\'' +
                ", signal=" + signal +
                ", value=" + value +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
