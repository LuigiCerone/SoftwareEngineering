package Model.DAO;

import Database.Database;
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
            connection = database.getConnection();

        String query = "INSERT INTO robot (robot.id, clusterId, ir, count, downTime) VALUES (?,?,?,?,?); ";
        try {
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, robot.getRobotId());
            statement.setString(2, robot.getClusterId());
            statement.setFloat(3, robot.getInefficiencyRate());
            statement.setInt(4, robot.getCountInefficiencyComponents());
            statement.setInt(5, robot.getDownTime());

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
        Connection connection = database.getConnection();
        Robot robot = null;

        String query = "SELECT * FROM robot WHERE robot.id = ? ;";
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
                robot.setInefficiencyRate(resultSet.getFloat("ir"));
                robot.setCountInefficiencyComponents(resultSet.getInt("count"));
                robot.setDownTime(resultSet.getInt("downTime"));
                // TODO add other meaningful attributes.
            }

            if (count == 0) {
                System.out.println("Robot not found.");
                // The cluster is not present, then we need to initialize it.
                robot = new Robot();
                robot.setRobotId(readData.getRobot());
                robot.setClusterId(readData.getCluster());
                robot.setInefficiencyRate((float) 0.0);
                robot.setCountInefficiencyComponents(0);
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