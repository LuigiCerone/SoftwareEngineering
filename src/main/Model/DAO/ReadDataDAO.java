package main.Model.DAO;

import main.Database.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ReadDataDAO implements ReadDataDAO_Interface {
    private DatabaseConnector databaseConnector;

    public ReadDataDAO() {
        this.databaseConnector = new DatabaseConnector();
    }

    @Override
    public void insert(main.Model.ReadData readData) {
        Connection connection = databaseConnector.getConnection();

        String sql = "INSERT INTO read_data (id, robotId, clusterId, zoneId, signals, value, timestamp) " +
                "VALUES (null, ?,?,?,?,?,?); ";
        System.out.println(sql);
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, readData.getRobot());
            statement.setString(2, readData.getCluster());
            statement.setString(3, readData.getZone());
            statement.setInt(4, readData.getSignal());
            statement.setBoolean(5, readData.getValue());
            statement.setTimestamp(6, readData.getTimestamp());

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        databaseConnector.closeConnectionToDB(connection);
    }

    @Override
    public main.Model.ReadData getByRobotId(int robotId) {
        return null;
    }

    @Override
    public main.Model.ReadData getByClusterId(int clusterId) {
        return null;
    }
}
