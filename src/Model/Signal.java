package Model;

public class Signal {
    private int singalNumber;
    private boolean value;
    private int robotId;

    public Signal(int singalNumber, boolean value, int robotId) {
        this.singalNumber = singalNumber;
        this.value = value;
        this.robotId = robotId;
    }

    public int getSingalNumber() {
        return singalNumber;
    }

    public void setSingalNumber(int singalNumber) {
        this.singalNumber = singalNumber;
    }

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public int getRobotId() {
        return robotId;
    }

    public void setRobotId(int robotId) {
        this.robotId = robotId;
    }
}
