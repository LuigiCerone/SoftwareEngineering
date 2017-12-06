package Model.DAO;

import Model.Cluster;
import Model.ReadData;

import java.sql.Connection;

public interface ClusterDAO_Interface {
    public void insert(Cluster cluster, Connection connection);
    public Cluster findClusterByIdOrInsert(ReadData readData);
}
