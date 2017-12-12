package main.Model.DAO;

import main.Model.Signal;

import java.util.HashMap;

public interface SignalDAO_Interface {
    public void update(Signal signal);

    public HashMap<Integer, Boolean> getAllSignalsForRobot(String robotId);
}
