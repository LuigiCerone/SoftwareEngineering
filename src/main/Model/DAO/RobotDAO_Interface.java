package main.Model.DAO;

import main.Model.ReadData;
import main.Model.Robot;

import java.util.HashMap;

public interface RobotDAO_Interface {

    void insert(ReadData readData);

    void updateCountAndStartDown(Robot robot, ReadData readData);

    void updateCountAndStopDown(Robot robot, ReadData readData);

    void updateCount(Robot robot);

    void updateIR(HashMap<String, Double> clustersIR);
}
