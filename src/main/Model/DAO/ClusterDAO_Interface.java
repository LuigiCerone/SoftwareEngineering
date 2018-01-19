package main.Model.DAO;

import main.Model.Cluster;
import main.Model.ReadData;

import java.util.HashMap;
import java.util.HashSet;

public interface ClusterDAO_Interface {

    void insert(ReadData readData);

    public Cluster findClusterByIdOrInsert(ReadData readData);

    public void updateCountAndStartDown(Cluster cluster, ReadData readData);

    public void updateCountAndStopDown(Cluster cluster, ReadData readData, long downTimeDiffCluster);

    public void processClusterIR();

    void updateIR(HashMap<String, Float> clustersIR);

    HashSet<Cluster> getAllClusters();

//    public void updateCount(Cluster cluster);
}
