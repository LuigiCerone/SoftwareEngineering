package Model;

import java.util.List;

public class Cluster {
    private String id;
    private String zoneId;
    private float inefficiencyRate;
    private boolean isUp;
    private List<Robot> robotsList;

    public Cluster(String id, String zoneId, float inefficiencyRate, boolean isUp, List<Robot> robotsList) {
        this.id = id;
        this.zoneId = zoneId;
        this.inefficiencyRate = inefficiencyRate;
        this.isUp = isUp;
        this.robotsList = robotsList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public float getInefficiencyRate() {
        return inefficiencyRate;
    }

    public void setInefficiencyRate(float inefficiencyRate) {
        this.inefficiencyRate = inefficiencyRate;
    }

    public boolean isUp() {
        return isUp;
    }

    public void setUp(boolean up) {
        isUp = up;
    }

    public List<Robot> getRobotsList() {
        return robotsList;
    }

    public void setRobotsList(List<Robot> robotsList) {
        this.robotsList = robotsList;
    }
}