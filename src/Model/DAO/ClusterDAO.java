package Model.DAO;

import Database.Database;
import Model.Cluster;
import Model.ReadData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClusterDAO implements ClusterDAO_Interface {
    private Database database;

    public ClusterDAO() {
        this.database = new Database();
    }

    @Override
    public void insert(Cluster cluster, Connection connection) {
        if (connection == null)
            connection = database.getConnection();

        String query = "INSERT INTO cluster (id, zoneId, ir) VALUES (?,?,?);";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, cluster.getId());
            statement.setString(2, cluster.getZoneId());
            statement.setFloat(3, cluster.getInefficiencyRate());

            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        database.closeConnectionToDB(connection);
    }

    @Override
    public Cluster findClusterByIdOrInsert(ReadData readData) {
        Connection connection = database.getConnection();
        Cluster cluster = null;

        String query = "SELECT * FROM cluster WHERE cluster.id = ? ;";
        try {
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, readData.getCluster());
            ResultSet resultSet = statement.executeQuery();

            int count = 0;

            while (resultSet.next()) {
                count++;
                cluster = new Cluster();

                cluster.setId(readData.getCluster());
                cluster.setZoneId(resultSet.getString("zoneId"));
                cluster.setInefficiencyRate(resultSet.getFloat("ir"));
                // TODO add other meaningful attributes.
            }

            if (count == 0) {
                System.out.println("Cluster not found.");
                // The cluster is not present, then we need to initialize it.
                cluster = new Cluster();
                cluster.setId(readData.getCluster());
                cluster.setZoneId(readData.getZone());
                cluster.setInefficiencyRate((float) 0.0);
                this.insert(cluster, connection);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (connection != null)
            database.closeConnectionToDB(connection);
        return cluster;
    }
}
