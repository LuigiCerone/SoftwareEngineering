package main.Model;

import com.google.gson.annotations.Expose;

import java.util.HashSet;

public class Zone {
    public static String ZONE_ID = "zoneId";

    @Expose
    private String id;
    @Expose
    private HashSet<Cluster> clustersList;

    public Zone(String id) {
        this.id = id;
        this.clustersList = new HashSet<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public HashSet<Cluster> getClustersList() {
        return clustersList;
    }

    public void addCluster(Cluster cluster) {
        this.clustersList.add(cluster);
    }

    @Override
    public String toString() {
        return " {" +
                "\"id\": \"" + id + "\"" +
                ", \"clustersList\": " + clustersList +
                "}";
    }
}
