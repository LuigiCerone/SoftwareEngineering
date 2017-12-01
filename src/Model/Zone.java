package Model;

import java.util.List;

public class Zone {
    private String id;
    private List<Cluster> clustersList;

    public Zone(String id, List<Cluster> clustersList) {
        this.id = id;
        this.clustersList = clustersList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Cluster> getClustersList() {
        return clustersList;
    }

    public void setClustersList(List<Cluster> clustersList) {
        this.clustersList = clustersList;
    }
}
