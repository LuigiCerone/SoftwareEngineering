package main.Model.DAO;

import main.Model.ReadData;
import main.Model.Robot;

import java.util.HashMap;
import java.util.HashSet;

public interface RobotDAO_Interface {

    HashSet<Robot> getRobotsForCluster(String clusterId);

    HashSet<Robot> getRobotsInfoNoHistory(String clusterId);

    void insert(ReadData readData);

    void updateCountAndStartDown(Robot robot, ReadData readData);

    void updateCountAndStopDown(Robot robot, ReadData readData);

    void updateCount(Robot robot);

    void updateIR(HashMap<String, Double> clustersIR);

    Robot getRobot(ReadData readData);
}
