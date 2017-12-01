package Model;

public class Robot {
    private String id;
    private String clusterId;
    private String zoneId;
    private boolean isUp;
    private float inefficiencyRate;


    public Robot(String id, String clusterId, String zoneId, boolean isUp, float inefficiencyRate) {
        this.id = id;
        this.clusterId = clusterId;
        this.zoneId = zoneId;
        this.isUp = isUp;
        this.inefficiencyRate = inefficiencyRate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public boolean isUp() {
        return isUp;
    }

    public void setUp(boolean up) {
        isUp = up;
    }

    public float getInefficiencyRate() {
        return inefficiencyRate;
    }

    public void setInefficiencyRate(float inefficiencyRate) {
        this.inefficiencyRate = inefficiencyRate;
    }
}
