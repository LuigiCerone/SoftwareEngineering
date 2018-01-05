package main.Model.DAO;

import main.Model.ReadData;
import main.Model.Robot;

import java.sql.Connection;
import java.util.List;

public interface RobotDAO_Interface {
    public void insert(Robot robot, Connection connection);

    public Robot get(int robotId);

    public Robot findRobotByIdOrInsert(ReadData readData);

    public void updateCountAndStartDown(Robot robot, ReadData readData);

    public void updateCountAndStopDown(Robot robot, ReadData readData, long downTimeDiff);

    public void updateCount(Robot robot);

    public long getDownTime(String robotId);

    public void delete(String p3Z);

    public void processRobotIR();

    public List<Robot> getAllRobots();
}
