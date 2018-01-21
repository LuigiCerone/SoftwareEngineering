package main.Model.DAO;

import main.Model.Cluster;
import main.Model.ReadData;

import java.util.HashMap;
import java.util.HashSet;

public interface ClusterDAO_Interface {

    Cluster getCluster(ReadData readData);

    HashSet<Cluster> getClustersForZone(String zoneId);

    void insert(ReadData readData);

    void updateCountAndStartDown(Cluster cluster, ReadData readData);

    void updateCountAndStopDown(Cluster cluster, ReadData readData);

    void updateIR(HashMap<String, Double> clustersIR);

    HashSet<Cluster> getAllClusters();

}
