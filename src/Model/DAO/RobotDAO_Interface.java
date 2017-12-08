package Model.DAO;

import Model.ReadData;
import Model.Robot;

import java.sql.Connection;

public interface RobotDAO_Interface {
    public void insert(Robot robot, Connection connection);

    public Robot get(int robotId);

    public Robot findRobotByIdOrInsert(ReadData readData);

    public void updateCountAndStartDown(Robot robot, ReadData readData);
}
