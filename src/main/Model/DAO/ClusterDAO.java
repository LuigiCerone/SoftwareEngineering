package main.Model.DAO;

import main.Database.Database;
import main.Model.Cluster;
import main.Model.ReadData;
import main.Model.Robot;

import java.sql.*;

public class ClusterDAO implements ClusterDAO_Interface {
    private Database database;

    public ClusterDAO() {
        this.database = new Database();
    }

    @Override
    public void insert(Cluster cluster, Connection connection) {
        if (connection == null)
            connection = database.getConnection();

        String query = "INSERT INTO cluster (id, zoneId, ir, count, downTime, startUpTime) VALUES (?,?,?,?,?,?);";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, cluster.getClusterId());
            statement.setString(2, cluster.getZoneId());
            statement.setFloat(3, cluster.getInefficiencyRate());
            statement.setInt(4, cluster.getCountInefficiencyComponents());
            statement.setLong(5, cluster.getDownTime());
            statement.setTimestamp(6, cluster.getStartUpTime());

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

                cluster.setClusterId(readData.getCluster());
                cluster.setZoneId(resultSet.getString(Cluster.CLUSTER_ID));
                cluster.setInefficiencyRate(resultSet.getFloat(Cluster.INEFFICIENCY_RATE));
                cluster.setCountInefficiencyComponents(resultSet.getInt(Cluster.COUNT_INEFFICIENCY_COMPONENTS));
                cluster.setDownTime(resultSet.getInt(Cluster.DOWN_TIME));
                cluster.setStartDownTime(resultSet.getTimestamp(Cluster.START_DOWN_TIME));
                cluster.setStartUpTime(resultSet.getTimestamp(Cluster.START_UP_TIME));
                // TODO add other meaningful attributes.
            }

            if (count == 0) {
                System.out.println("Cluster not found.");
                // The cluster is not present, then we need to initialize it.
                cluster = new Cluster();
                cluster.setClusterId(readData.getCluster());
                cluster.setZoneId(readData.getZone());
                cluster.setInefficiencyRate((float) 0.0);
                cluster.setCountInefficiencyComponents(0);
                cluster.setDownTime(0);
                cluster.setStartUpTime(new Timestamp(System.currentTimeMillis()));
                this.insert(cluster, connection);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (connection != null)
            database.closeConnectionToDB(connection);
        return cluster;
    }

    @Override
    public void updateCountAndStartDown(Cluster cluster, ReadData readData) {
        Connection connection = database.getConnection();

        String query = "UPDATE cluster " +
                "SET count=? , startDownTime=? " +
                "WHERE id = ?;";

        try {
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setInt(1, cluster.getCountInefficiencyComponents());
            statement.setTimestamp(2, readData.getTimestamp());
            statement.setString(3, cluster.getClusterId());

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        database.closeConnectionToDB(connection);

    }

    @Override
    public void updateCountAndStopDown(Cluster cluster, ReadData readData, long downTimeDiffCluster) {
        Connection connection = database.getConnection();

        String query = "UPDATE cluster " +
                "SET count=? ,  startDownTime=? , downTime=? " +
                "WHERE id=? ";
        try {
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setInt(1, cluster.getCountInefficiencyComponents());
            statement.setTimestamp(2, null);
            statement.setLong(3, downTimeDiffCluster);
            statement.setString(4, cluster.getClusterId());

            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        database.closeConnectionToDB(connection);
    }

    @Override
    public void processClusterIR() {

    }

//    @Override
//    public void updateCount(Cluster cluster) {
//        Connection connection = database.getConnection();
//
//        String query = "UPDATE cluster " +
//                "SET count = ? " +
//                "WHERE id=?; ";
//
//        try {
//            PreparedStatement statement = connection.prepareStatement(query);
//
//            statement.setInt(1, cluster.getCountInefficiencyComponents());
//            statement.setString(2, cluster.getRobotId());
//
//            statement.execute();
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
}
