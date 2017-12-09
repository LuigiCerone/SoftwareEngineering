package main.Database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.*;

public class Database {
    public static final String DATABASE_NAME = "SEProject";
    public static final String DRIVER = "com.mysql.jdbc.Driver";
    protected static String URL = "jdbc:mysql://localhost/SEProject";
    protected static String USER = "se";
    protected static String PSW = "se";

    private static DataSource datasource;

    public Database() {

    }

//    private static final Logger LOGGER = LoggerFactory.getLogger(MyClass.class);
//    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static DataSource getDataSource() {
        if (datasource == null)
        {
            HikariConfig config = new HikariConfig();

            config.setDriverClassName("com.mysql.jdbc.Driver");
            config.setJdbcUrl(URL);
            config.setUsername(USER);
            config.setPassword(PSW);
            config.setMaximumPoolSize(20);

            datasource = new HikariDataSource(config);
        }

        return datasource;

    }

    public Connection getConnection(){
        try {
            return getDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
//
//    public Connection connectToDB() {
//        Connection con = null;
//        try {
//            // Carichiamo un driver di tipo 1 (bridge jdbc-odbc).
//            //String driver = "sun.jdbc.odbc.JdbcOdbcDriver";
//            Class.forName(DRIVER);
//            // Creiamo la stringa di connessione.
//            // Otteniamo una connessione con username e password.
//            con = DriverManager.getConnection(URL, USER, PSW);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        return con;
//    }

    public void closeConnectionToDB(Connection con) {
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
