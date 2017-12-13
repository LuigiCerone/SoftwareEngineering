package main.Model.DAO;

import main.Database.Database;
import main.Main.Util;
import main.Model.ReadData;
import main.Model.Robot;

import java.sql.*;

public class RobotDAO implements RobotDAO_Interface {
    private Database database;

    public RobotDAO() {
        this.database = new Database();
    }

    @Override
    public void insert(Robot robot, Connection connection) {
        if (connection == null)
            connection = database.getConnection();

        String query = "INSERT INTO robot (robot.id, clusterId, ir, count, downTime, startUpTime) VALUES (?,?,?,?,?,?); ";
        try {
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, robot.getRobotId());
            statement.setString(2, robot.getClusterId());
            statement.setFloat(3, robot.getInefficiencyRate());
            statement.setInt(4, robot.getCountInefficiencyComponents());
            statement.setInt(5, robot.getDownTime());
            statement.setTimestamp(6, robot.getStartUpTime());

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
                // There is already a robot in the DB.

                robot = new Robot();

                robot.setRobotId(readData.getRobot());
                robot.setClusterId(resultSet.getString(Robot.CLUSTER_ID));
                robot.setInefficiencyRate(resultSet.getFloat(Robot.INEFFICIENCY_RATE));
                robot.setCountInefficiencyComponents(resultSet.getInt(Robot.COUNT_INEFFICIENCY_COMPONENTS));
                robot.setDownTime(resultSet.getInt(Robot.DOWN_TIME));
                robot.setStartDownTime(resultSet.getTimestamp(Robot.START_DOWN_TIME));
                robot.setStartUpTime(resultSet.getTimestamp(Robot.START_UP_TIME));
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
                robot.setStartUpTime(new Timestamp(System.currentTimeMillis()));

                this.insert(robot, connection);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (connection != null)
            database.closeConnectionToDB(connection);
        return robot;
    }

    @Override
    public void updateCountAndStartDown(Robot robot, ReadData readData) {
        Connection connection = database.getConnection();

        String query = "UPDATE robot " +
                "SET count=? , startDownTime=? " +
                "WHERE id = ?;";

        try {
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setInt(1, robot.getCountInefficiencyComponents());
            statement.setTimestamp(2, readData.getTimestamp());
            statement.setString(3, robot.getRobotId());

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        database.closeConnectionToDB(connection);
    }

    public void updateCountAndStopDown(Robot robot, ReadData readData, long downTimeDiff) {
        Connection connection = database.getConnection();

        String query = "UPDATE robot " +
                "SET count=? ,  startDownTime=? , downTime=? " +
                "WHERE id=? ";
        try {
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setInt(1, robot.getCountInefficiencyComponents());
            statement.setTimestamp(2, null);
            statement.setLong(3, downTimeDiff);
            statement.setString(4, robot.getRobotId());

            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        database.closeConnectionToDB(connection);
    }

    public void updateCount(Robot robot) {
        Connection connection = database.getConnection();

        String query = "UPDATE robot " +
                "SET count = ? " +
                "WHERE id=?; ";

        try {
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setInt(1, robot.getCountInefficiencyComponents());
            statement.setString(2, robot.getRobotId());

            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        database.closeConnectionToDB(connection);
    }

    @Override
    public long getDownTime(String robotId) {
        Connection connection = database.getConnection();

        String query = "SELECT downTime " +
                "FROM robot " +
                "WHERE id = ?;";
        long downTime = 0;

        try {
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, robotId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                downTime = resultSet.getLong(Robot.DOWN_TIME);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        database.closeConnectionToDB(connection);
        return downTime;
    }

    @Override
    public void delete(String robotId) {
        Connection connection = database.getConnection();

        String query = "DELETE FROM robot " +
                "WHERE id=?;";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, robotId);
            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        database.closeConnectionToDB(connection);
    }

    @Override
    public void processRobotIR() {
        Connection connection = database.getConnection();

        String query = "SELECT id, downTime, startUpTime, startDownTime" +
                " FROM robot;";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                Robot robot = new Robot();
                
                robot.setRobotId(resultSet.getString(Robot.ROBOT_ID));
                robot.setDownTime(resultSet.getInt(Robot.DOWN_TIME));
                robot.setStartUpTime(resultSet.getTimestamp(Robot.START_UP_TIME));
                robot.setStartDownTime(resultSet.getTimestamp(Robot.START_DOWN_TIME));
                
                calculateIR(robot);
            }
        } catch (SQLException e) {
            
        }
        


    }

    private void calculateIR(Robot robot) {
        Timestamp now = new Timestamp(System.currentTimeMillis());

        long upTime = Util.differenceBetweenTimestamps(now, robot.getStartUpTime());
        long downTime = robot.getDownTime();

        // Is the robot still down?
        if(robot.getStartDownTime() != null){
            // Robot is still down.
            downTime += Util.differenceBetweenTimestamps(now, robot.getStartDownTime());
        }

        float ir = (downTime / upTime) * 100;
        robot.setInefficiencyRate(ir);

        // TODO Insert into DB.
        updateRobotWithIR(robot);
    }

    private void updateRobotWithIR(Robot robot) {
        Connection connection = database.getConnection();

        String query = "UPDATE robot" +
                " SET ir = ? " +
                " WHERE id = ?;";

        try {
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setFloat(1, robot.getInefficiencyRate());
            statement.setString(2, robot.getRobotId());
            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
