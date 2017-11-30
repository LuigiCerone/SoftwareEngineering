package Model;

public class Cluster {
    private int id;
    private float inefficiencyRate;
    private int zoneId;

    public Cluster(int id, float inefficiencyRate, int zoneId) {
        this.id = id;
        this.inefficiencyRate = inefficiencyRate;
        this.zoneId = zoneId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getInefficiencyRate() {
        return inefficiencyRate;
    }

    public void setInefficiencyRate(float inefficiencyRate) {
        this.inefficiencyRate = inefficiencyRate;
    }

    public int getZoneId() {
        return zoneId;
    }

    public void setZoneId(int zoneId) {
        this.zoneId = zoneId;
    }
}
