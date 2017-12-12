package main.Model.DAO;

import main.Model.ReadData;

public interface ReadDataDAO_Interface {
    public void insert(ReadData readData);
    public ReadData getByRobotId(int robotId);
    public ReadData getByClusterId(int clusterId);
}