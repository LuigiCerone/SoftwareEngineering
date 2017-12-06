package Database;

import Model.ReadData;

import java.sql.*;

public class Database {
    public static final String DATABASE_NAME = "SEProject";
    public static final String DRIVER = "com.mysql.jdbc.Driver";
    protected static String URL = "jdbc:mysql://localhost/SEProject";
    protected static String USER = "se";
    protected static String PSW = "se";

    private Connection databaseConnection;
    private PreparedStatement statement;

    public Database() {
        if (databaseConnection == null) {
            this.databaseConnection = connectToDB();
        }
    }

    public static Connection connectToDB() {
        Connection con = null;
        try {
            // Carichiamo un driver di tipo 1 (bridge jdbc-odbc).
            //String driver = "sun.jdbc.odbc.JdbcOdbcDriver";
            Class.forName(DRIVER);
            // Creiamo la stringa di connessione.
            // Otteniamo una connessione con username e password.
            con = DriverManager.getConnection(URL, USER, PSW);
            System.out.println("OK");

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return con;
    }

    public void insertReadData(ReadData readData) {
        String sql = "INSERT INTO read_data (id, robotId, clusterId, zoneId, signals, value, timestamp) " +
                "VALUES (null, ?,?,?,?,?, NOW()); ";
        System.out.println(sql);
        try {
            statement = this.databaseConnection.prepareStatement(sql);
            statement.setString(1, readData.getRobot());
            statement.setString(2, readData.getCluster());
            statement.setString(3, readData.getZone());
            statement.setInt(4, readData.getSignal());
            statement.setInt(5, readData.getValue());
//            statement.setString(6, readData.getTimestamp());

            statement.execute();


        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
