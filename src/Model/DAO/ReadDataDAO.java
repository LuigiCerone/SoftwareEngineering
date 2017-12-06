package Model.DAO;

import Database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ReadDataDAO implements ReadDataDAO_Interface {
    private Database database;

    public ReadDataDAO() {
        this.database = new Database();
    }

    @Override
    public void insert(Model.ReadData readData) {
        Connection connection = database.connectToDB();

        String sql = "INSERT INTO read_data (id, robotId, clusterId, zoneId, signals, value, timestamp) " +
                "VALUES (null, ?,?,?,?,?,?); ";
        System.out.println(sql);
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, readData.getRobot());
            statement.setString(2, readData.getCluster());
            statement.setString(3, readData.getZone());
            statement.setInt(4, readData.getSignal());
            statement.setInt(5, readData.getValue());
            statement.setString(6, readData.getTimestamp());

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        database.closeConnectionToDB(connection);
    }

    @Override
    public Model.ReadData getByRobotId(int robotId) {
        return null;
    }

    @Override
    public Model.ReadData getByClusterId(int clusterId) {
        return null;
    }
}
