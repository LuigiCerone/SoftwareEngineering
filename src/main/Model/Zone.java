package main.Model;

import com.google.gson.annotations.Expose;

import java.util.HashMap;

public class Zone {
    public static String ZONE_ID = "zoneId";

    @Expose
    private String id;
    @Expose
    private HashMap<String, Cluster> clustersList;

    public Zone(String id) {
        this.id = id;
        this.clustersList = new HashMap<>();
    }

    public Zone(String id, HashMap<String, Cluster> clusters) {
        this.id = id;
        this.clustersList = clusters;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public HashMap<String, Cluster> getClustersList() {
        return clustersList;
    }

    public Cluster getCluster(String clusterId) {
        return clustersList.get(clusterId);
    }

    public void addCluster(String zoneId, Cluster cluster) {
        this.clustersList.put(zoneId, cluster);
    }

    @Override
    public String toString() {
        return " {" +
                "\"id\": \"" + id + "\"" +
                ", \"clustersList\": " + clustersList +
                "}";
    }
}
