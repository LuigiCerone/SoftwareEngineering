package main.Model.DAO;

import main.Model.Signal;

public interface SignalDAO_Interface {
    public void update(Signal signal);

    public Signal[] getAllSignalsForRobot(String robotId);
}
