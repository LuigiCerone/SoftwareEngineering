package Model.DAO;

import Database.Database;
import Model.Cluster;
import Model.ReadData;
import Model.Robot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RobotDAO implements RobotDAO_Interface {
    private Database database;

    public RobotDAO() {
        this.database = new Database();
    }

    @Override
    public void insert(Robot robot, Connection connection) {
        if (connection == null)
            connection = database.connectToDB();

        String query = "INSERT INTO robot (id, clusterId, zoneId, ir, count, downTime) VALUES (?,?,?,?,?,?); ";
        try {
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, robot.getRobotId());
            statement.setString(2, robot.getClusterId());
            statement.setString(3, robot.getZoneId());
            statement.setFloat(4, robot.getInefficiencyRate());
            statement.setInt(5, robot.getCountInefficencyComponents());
            statement.setInt(6, robot.getDownTime());

            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        database.closeConnectionToDB(connection);
    }

    @Override
    public Robot get(int robotId) {
        return null;
    }

    @Override
    public Robot findRobotByIdOrInsert(ReadData readData) {
        Connection connection = database.connectToDB();
        Robot robot = null;

        String query = "SELECT COUNT(*) FROM robot WHERE robot.id = ? ;";
        try {
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, readData.getRobot());
            ResultSet resultSet = statement.executeQuery();

            int count = 0;

            while (resultSet.next()) {
                count++;
                robot = new Robot();

                robot.setRobotId(readData.getRobot());
                robot.setClusterId(resultSet.getString("clusterId"));
                robot.setZoneId(resultSet.getString("zoneId"));
                robot.setInefficiencyRate(resultSet.getFloat("ir"));
                robot.setCountInefficencyComponents(resultSet.getInt("count"));
                robot.setDownTime(resultSet.getInt("downTime"));
                // TODO add other meaningful attributes.
            }

            if (count == 0) {
                System.out.println("Robot not found.");
                // The cluster is not present, then we need to initialize it.
                robot = new Robot();
                robot.setRobotId(readData.getRobot());
                robot.setClusterId(readData.getCluster());
                robot.setZoneId(readData.getZone());
                robot.setInefficiencyRate((float) 0.0);
                robot.setCountInefficencyComponents(0);
                robot.setDownTime(0);

                this.insert(robot, connection);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (connection != null)
            database.closeConnectionToDB(connection);
        return robot;
    }
}
