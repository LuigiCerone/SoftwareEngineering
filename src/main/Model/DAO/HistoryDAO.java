package main.Model.DAO;

import main.Database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class HistoryDAO implements HistoryDAO_Interface {
    private Database database;

    public HistoryDAO() {
        this.database = new Database();
    }

    @Override
    public void insertPeriodStart(String deviceId, Timestamp start, boolean status) {
        Connection connection = database.getConnection();

        String query = "INSERT INTO history (deviceId, startTime, status) " +
                "VALUES (?,?,?);";

        try {
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, deviceId);
            statement.setTimestamp(2, start);
            statement.setBoolean(3, status);

            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        database.closeConnectionToDB(connection);
    }

    @Override
    public void insertPeriodEnd(String deviceId, Timestamp end, boolean status) {
        Connection connection = database.getConnection();

        String query = "UPDATE history " +
                "SET endTime=? " +
                "WHERE deviceId = ? AND status=? AND endTime IS NULL AND startTime IS NOT NULL;";

        try {
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setTimestamp(1, end);
            statement.setString(2, deviceId);
            statement.setBoolean(3, status);

            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        database.closeConnectionToDB(connection);
    }

}
