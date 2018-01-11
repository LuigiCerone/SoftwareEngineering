public class Robot {
    private String robotId;
    private String clusterId;
    private String zoneId;
    private boolean[] signals;


    public Robot(String robotId, String clusterId, String zoneId, boolean[] signals) {
        this.robotId = robotId;
        this.clusterId = clusterId;
        this.zoneId = zoneId;
        this.signals = signals;
    }

    public String getRobotId() {
        return robotId;
    }

    public void setRobotId(String robotId) {
        this.robotId = robotId;
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

    public boolean getSignal(int index) {
        return signals[index];
    }

    public void setSignals(boolean[] signals) {
        this.signals = signals;
    }

    public void invertSignal(int signalNumber) {
        this.signals[signalNumber] = !this.signals[signalNumber];
    }
}
