package main.Model.DAO;

import main.Database.Database;
import main.Main.Util;
import main.Model.Cluster;
import main.Model.ReadData;
import main.Model.Robot;

import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

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

                // Also an entry in the table history has to be created.
                new HistoryDAO().insertPeriodStart(cluster.getClusterId(), cluster.getStartUpTime(), true, 1);
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
        Connection connection = database.getConnection();
        Queue<Cluster> queue = new LinkedList<Cluster>();

        String query = "SELECT id, downTime, startUpTime, startDownTime" +
                " FROM cluster;";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Cluster cluster = new Cluster();

                cluster.setClusterId(resultSet.getString(Cluster.CLUSTER_ID));
                cluster.setDownTime(resultSet.getInt(Cluster.DOWN_TIME));
                cluster.setStartUpTime(resultSet.getTimestamp(Cluster.START_UP_TIME));
                cluster.setStartDownTime(resultSet.getTimestamp(Cluster.START_DOWN_TIME));

                queue.add(cluster);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        database.closeConnectionToDB(connection);
        calculateIR(queue);

    }

    private void calculateIR(Queue<Cluster> queue) {

        Timestamp now = new Timestamp(System.currentTimeMillis());
        Connection connection = database.getConnection();
        PreparedStatement statement = null;

        String query = "UPDATE cluster" +
                " SET ir = ? " +
                " WHERE id = ?;";

        try {
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(query);

            while (!queue.isEmpty()) {
                Cluster cluster = queue.remove();

//                long upTime = Util.differenceBetweenTimestamps(now, robot.getStartUpTime());
                long downTime = cluster.getDownTime();

                // Is the robot still down?
                if (cluster.getStartDownTime() != null) {
                    // Robot is still down.
                    downTime += Util.differenceBetweenTimestamps(now, cluster.getStartDownTime());
                }

//                float ir = (downTime / upTime) * 100;

                // downTime is in second so I need to cast it in minutes.
                downTime = downTime / 60;

                // 60 is the temporal window.
                float ir = ((float) downTime / 60) * 100;
                ir = (float) (Math.round(ir * 100.0) / 100.0);
                cluster.setInefficiencyRate(ir);

                statement.setFloat(1, cluster.getInefficiencyRate());
                statement.setString(2, cluster.getClusterId());
                statement.addBatch();
            }

            statement.executeBatch();
            connection.commit();
            System.out.println("Fatto!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        database.closeConnectionToDB(connection);
    }

    @Override
    public void updateIR(HashMap<String, Float> clustersIR) {
        Connection connection = database.getConnection();

        PreparedStatement statement = null;

        String query = "UPDATE cluster" +
                " SET ir = ? WHERE id = ?;";

        try {
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(query);

            for (Map.Entry<String, Float> entry : clustersIR.entrySet()) {
                statement.setFloat(1, entry.getValue());
                statement.setString(2, entry.getKey());
                statement.addBatch();
            }
            statement.executeBatch();
            connection.commit();
            System.out.println("Fatto!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        database.closeConnectionToDB(connection);
    }

    @Override
    public LinkedList<Cluster> getAllClusters() {
        Connection connection = database.getConnection();
        PreparedStatement preparedStatement = null;

        String query = "SELECT id, zoneId, ir FROM cluster;";
        LinkedList<Cluster> clusterLinkedList = new LinkedList<>();

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                clusterLinkedList.add(
                        new Cluster(
                                resultSet.getString(Cluster.CLUSTER_ID),
                                resultSet.getString(Cluster.ZONE_ID),
                                resultSet.getFloat(Cluster.INEFFICIENCY_RATE)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        database.closeConnectionToDB(connection);

        return clusterLinkedList;
    }
}
