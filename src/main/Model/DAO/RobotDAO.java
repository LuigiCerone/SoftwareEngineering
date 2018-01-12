package main.Model.DAO;

import main.Database.Database;
import main.Main.Util;
import main.Model.History;
import main.Model.ReadData;
import main.Model.Robot;

import java.math.BigDecimal;
import java.sql.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

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
                // Also an entry in the table history has to be created.
                new HistoryDAO().insertPeriodStart(robot.getRobotId(), robot.getStartUpTime(), true);

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

        Timestamp now = new Timestamp(System.currentTimeMillis());
        ZonedDateTime zonedDateTime = now.toInstant().atZone(ZoneId.of("UTC"));
        Timestamp oneHourAgo = Timestamp.from(zonedDateTime.minus(1, ChronoUnit.HOURS).toInstant());
        System.out.println("Timestamps are oneHourAgo: " + oneHourAgo.toString() + " now: " + now.toString());

        String query = "SELECT * FROM history " +
                "WHERE startTime>= ? OR endTime>=? OR endTime IS NULL ORDER BY deviceId;";

        HashMap<String, LinkedList<History>> map = new HashMap<>();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setTimestamp(1, oneHourAgo);
            preparedStatement.setTimestamp(2, oneHourAgo);


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

                if (resultSet.getTimestamp(History.START).before(oneHourAgo))
                    history.setStart(null);
                else
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

        computeIR(map, now, oneHourAgo);
    }

    private void computeIR(HashMap<String, LinkedList<History>> map, Timestamp now, Timestamp oneHourAgo) {

        for (Map.Entry<String, LinkedList<History>> entry : map.entrySet()) {
            LinkedList<History> histories = entry.getValue();
            String deviceId = entry.getKey();

            long upTime = 0;
            long downTime = 0;

            Collections.sort(histories, new History.HistoryComparator()); // Descending order based on history.ID .
            for (History history : histories) {
                if (history.getEnd() == null) {
                    long time = Util.differenceBetweenTimestamps(now, history.getStart());
                    if (time >= 3600) {
                        // This means the robot has been in this state for the last hour (and is still in this status).
                        if (history.getStatus())
                            upTime = 3600;
                        else downTime = 3600;
                    } else {
                        // This mean this is the currently status.
                        if (history.getStatus())
                            upTime += time;
                        else
                            downTime += time;
                    }
                } else if (history.getStart().after(oneHourAgo) && history.getEnd().before(now)) {
                    // This means that both startTime and endTime are in last hour.
                    long time = Util.differenceBetweenTimestamps(history.getEnd(), history.getStart());

                    if (history.getStatus())
                        upTime += time;
                    else
                        downTime += time;
                } else if (history.getStart() == null) {
                    // This means that the entry is on the middle.
                    long time = 3600 - downTime - upTime;
                    if (history.getStatus())
                        upTime += time;
                    else
                        downTime += time;
                }
            }

            downTime = downTime / 60; // downTime in minutes.

            // 60 is the temporal window.
            float ir = ((float) downTime / 60) * 100;
            ir = (float) (Math.round(ir * 100.0) / 100.0);


//            robot.setInefficiencyRate(ir);
            // TODO Save the IR, the ID is in deviceId (either robot or cluster).
        }
    }

//    @Override
//    public void processRobotIR() {
//        Connection connection = database.getConnection();
//        Queue<Robot> queue = new LinkedList<Robot>();
//
//        String query = "SELECT id, downTime, startUpTime, startDownTime" +
//                " FROM robot;";
//
//        try {
//            PreparedStatement statement = connection.prepareStatement(query);
//            ResultSet resultSet = statement.executeQuery();
//
//            while (resultSet.next()) {
//                Robot robot = new Robot();
//
//                robot.setRobotId(resultSet.getString(Robot.ROBOT_ID));
//                robot.setDownTime(resultSet.getInt(Robot.DOWN_TIME));
//                robot.setStartUpTime(resultSet.getTimestamp(Robot.START_UP_TIME));
//                robot.setStartDownTime(resultSet.getTimestamp(Robot.START_DOWN_TIME));
//
//                queue.add(robot);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        database.closeConnectionToDB(connection);
//        calculateIR(queue);
//
//    }

    @Override
    public List<Robot> getAllRobots() {
        Connection connection = database.getConnection();
        PreparedStatement preparedStatement = null;

        String query = "SELECT id, clusterId, ir FROM robot;";
        LinkedList<Robot> robotLinkedList = new LinkedList<>();

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                robotLinkedList.add(
                        new Robot(resultSet.getString(Robot.ROBOT_ID),
                                resultSet.getString(Robot.CLUSTER_ID),
                                resultSet.getFloat(Robot.INEFFICIENCY_RATE)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        database.closeConnectionToDB(connection);

        return robotLinkedList;
    }

//    private void calculateIR(Queue<Robot> queue) {
//        Timestamp now = new Timestamp(System.currentTimeMillis());
//        Connection connection = database.getConnection();
//        PreparedStatement statement = null;
//
//        String query = "UPDATE robot" +
//                " SET ir = ? " +
//                " WHERE id = ?;";
//
//        try {
//            connection.setAutoCommit(false);
//            statement = connection.prepareStatement(query);
//
//            while (!queue.isEmpty()) {
//                Robot robot = queue.remove();
//
////                long upTime = Util.differenceBetweenTimestamps(now, robot.getStartUpTime());
//                long downTime = robot.getDownTime();
//
//                // Is the robot still down?
//                if (robot.getStartDownTime() != null) {
//                    // Robot is still down.
//                    downTime += Util.differenceBetweenTimestamps(now, robot.getStartDownTime());
//                }
//
////                float ir = (downTime / upTime) * 100;
//
//                // downTime is in second so I need to cast it in minutes.
//                downTime = downTime / 60;
//
//                // 60 is the temporal window.
//                float ir = ((float) downTime / 60) * 100;
//                ir = (float) (Math.round(ir * 100.0) / 100.0);
//                robot.setInefficiencyRate(ir);
//
//                statement.setFloat(1, robot.getInefficiencyRate());
//                statement.setString(2, robot.getRobotId());
//                statement.addBatch();
//            }
//
//            statement.executeBatch();
//            connection.commit();
//            System.out.println("Fatto!");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        database.closeConnectionToDB(connection);
//    }
}
