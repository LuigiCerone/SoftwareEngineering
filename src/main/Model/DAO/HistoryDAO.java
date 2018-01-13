package main.Model.DAO;

import main.Database.Database;
import main.Model.History;

import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;

public class HistoryDAO implements HistoryDAO_Interface {
    private Database database;

    public HistoryDAO() {
        this.database = new Database();
    }

    @Override
    public void insertPeriodStart(String deviceId, Timestamp start, boolean status, int type) {
        Connection connection = database.getConnection();

        String query = "INSERT INTO history (deviceId, startTime, status, type) " +
                "VALUES (?,?,?,?);";

        try {
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, deviceId);
            statement.setTimestamp(2, start);
            statement.setBoolean(3, status);
            statement.setInt(4, type);

            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        database.closeConnectionToDB(connection);
    }

    // type=0 means Robot, type=1 means Cluster.
    @Override
    public void insertPeriodEnd(String deviceId, Timestamp end, boolean status, int type) {
        Connection connection = database.getConnection();

        String query = "UPDATE history " +
                "SET endTime=? " +
                "WHERE deviceId = ? AND status=? AND endTime IS NULL AND startTime IS NOT NULL AND type=?;";

        try {
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setTimestamp(1, end);
            statement.setString(2, deviceId);
            statement.setBoolean(3, status);
            statement.setInt(4, type);

            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        database.closeConnectionToDB(connection);
    }

    // If type = 1 then clusters' IR will be calculated, else if type = 0 robots' IR will be.
    @Override
    public HashMap<String, LinkedList<History>> processIR(Timestamp now, Timestamp oneHourAgo, int type) {
        Connection connection = database.getConnection();

        String query = "SELECT * FROM history " +
                "WHERE type=? AND (startTime>= ? OR endTime>=? OR endTime IS NULL)  " +
                "ORDER BY deviceId;";

        HashMap<String, LinkedList<History>> map = new HashMap<>();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, type);
            preparedStatement.setTimestamp(2, oneHourAgo);
            preparedStatement.setTimestamp(3, oneHourAgo);


            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String deviceId = resultSet.getString(History.DEVICE_ID);

                if (!map.containsKey(deviceId)) {
                    map.put(deviceId, new LinkedList<History>());
                }
                LinkedList<History> histories = map.get(deviceId);

                History history = new History();
                history.setId(resultSet.getInt(History.HISTORY_ID));
                history.setDeviceId(deviceId);

//                if (resultSet.getTimestamp(History.START).before(oneHourAgo))
//                    history.setStart(null);
//                else
//                    history.setStart(resultSet.getTimestamp(History.START));
                history.setStart(resultSet.getTimestamp(History.START));

                history.setEnd(resultSet.getTimestamp(History.END));
                history.setStatus(resultSet.getBoolean(History.STATUS));

                histories.add(history);

                map.put(deviceId, histories); // Update. Maybe this isn't needed.
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        database.closeConnectionToDB(connection);
        return map;
    }
}
