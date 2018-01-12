package main.Model.DAO;

import main.Model.Cluster;
import main.Model.ReadData;

import java.sql.Connection;
import java.util.HashMap;

public interface ClusterDAO_Interface {
    public void insert(Cluster cluster, Connection connection);

    public Cluster findClusterByIdOrInsert(ReadData readData);

    public void updateCountAndStartDown(Cluster cluster, ReadData readData);

    public void updateCountAndStopDown(Cluster cluster, ReadData readData, long downTimeDiffCluster);

    public void processClusterIR();

    void updateIR(HashMap<String, Float> clustersIR);

//    public void updateCount(Cluster cluster);
}
