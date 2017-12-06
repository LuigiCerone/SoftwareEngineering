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

    }

    public Connection connectToDB() {
        Connection con = null;
        try {
            // Carichiamo un driver di tipo 1 (bridge jdbc-odbc).
            //String driver = "sun.jdbc.odbc.JdbcOdbcDriver";
            Class.forName(DRIVER);
            // Creiamo la stringa di connessione.
            // Otteniamo una connessione con username e password.
            con = DriverManager.getConnection(URL, USER, PSW);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return con;
    }

    public void closeConnectionToDB(Connection con) {
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
