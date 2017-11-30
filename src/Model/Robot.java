package Model;

public class Robot {
    private int id;
    private int clusterId;
    private int zoneId;
    private float inefficiencyRate;

    public Robot(int id, int clusterId, int zoneId, float inefficiencyRate) {
        this.id = id;
        this.clusterId = clusterId;
        this.zoneId = zoneId;
        this.inefficiencyRate = inefficiencyRate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClusterId() {
        return clusterId;
    }

    public void setClusterId(int clusterId) {
        this.clusterId = clusterId;
    }

    public int getZoneId() {
        return zoneId;
    }

    public void setZoneId(int zoneId) {
        this.zoneId = zoneId;
    }

    public float getInefficiencyRate() {
        return inefficiencyRate;
    }

    public void setInefficiencyRate(float inefficiencyRate) {
        this.inefficiencyRate = inefficiencyRate;
    }
}
