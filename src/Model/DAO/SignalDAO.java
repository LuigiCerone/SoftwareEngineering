package Model.DAO;

import Database.Database;
import Model.Signal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SignalDAO implements SignalDAO_Interface {
    private Database database;

    public SignalDAO() {
        this.database = new Database();
    }

    @Override
    public void update(Signal signal) {
        Connection conn = database.connectToDB();
        String query = "UPDATE signals " +
                "SET value=? AND timestamp=? " +
                "WHERE robotId = ? AND number=?; ";

        try {
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setBoolean(1, signal.getSignalValue());
            statement.setString(2, signal.getTimestamps());
            statement.setString(3, signal.getRobotId());
            statement.setInt(4, signal.getSingalNumber());

            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
