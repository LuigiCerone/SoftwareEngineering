package main.Model.DAO;

import main.Database.Database;
import main.Model.Signal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class SignalDAO implements SignalDAO_Interface {
    
    @Override
    public void update(Signal signal) {
        Connection connection = Database.getConnection();
        String query = "UPDATE signals " +
                "SET value=? , timestamp=? " +
                "WHERE robotId = ? AND number=?; ";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setBoolean(1, signal.getSignalValue());
            statement.setTimestamp(2, signal.getTimestamps());
            statement.setString(3, signal.getRobotId());
            statement.setInt(4, signal.getSingalNumber());

            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        Database.closeConnectionToDB(connection);
    }

    @Override
    public HashMap<Integer, Boolean> getAllSignalsForRobot(String robotId) {
        Connection connection = Database.getConnection();

        String query = "SELECT robotId, number, value " +
                "FROM signals " +
                "WHERE robotId = ?;";

        HashMap<Integer, Boolean> robotSignal = new HashMap<Integer, Boolean>();

        try {
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, robotId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                robotSignal.put(resultSet.getInt(Signal.SIGNAL_NUMER), resultSet.getBoolean(Signal.SIGNAL_VALUE));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        Database.closeConnectionToDB(connection);
        return robotSignal;
    }
}
