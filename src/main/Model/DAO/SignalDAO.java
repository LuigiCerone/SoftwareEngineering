package main.Model.DAO;

import main.Database.Database;
import main.Model.Signal;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SignalDAO implements SignalDAO_Interface {
    private Database database;

    public SignalDAO() {
        this.database = new Database();
    }

    @Override
    public void update(Signal signal) {
        Connection conn = database.getConnection();
        String query = "UPDATE signals " +
                "SET value=? , timestamp=? " +
                "WHERE robotId = ? AND number=?; ";

        try {
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setBoolean(1, signal.getSignalValue());
            statement.setTimestamp(2, signal.getTimestamps());
            statement.setString(3, signal.getRobotId());
            statement.setInt(4, signal.getSingalNumber());

            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Signal[] getAllSignalsForRobot(String robotId) {
        Connection connection = database.getConnection();

        String query = "SELECT robotId, number, value " +
                "FROM signals " +
                "WHERE robotId = ?;";

        Signal[] robotSignals = new Signal[7];

        try {
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, robotId);
            ResultSet resultSet = statement.executeQuery();

            int i = 0;
            while (resultSet.next()) {
                Signal signal = new Signal();
                signal.setSingalNumber(resultSet.getInt(Signal.SIGNAL_NUMER));
                signal.setSignalValue(resultSet.getBoolean(Signal.SIGNAL_VALUE));

                robotSignals[i] = signal;
                i++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return robotSignals;
    }
}
