package main.Model;

import com.google.gson.annotations.Expose;

import java.util.HashSet;

public class Zone {
    public static String ZONE_ID = "zoneId";

    @Expose
    private String id;
    @Expose
    private HashSet<Cluster> clustersList;

    public Zone(String id, HashSet<Cluster> clusters) {
        this.id = id;
        this.clustersList = clusters;
    }

    @Override
    public String toString() {
        return " {" +
                "\"id\": \"" + id + "\"" +
                ", \"clustersList\": " + clustersList +
                "}";
    }
}
