package main.Model.DAO;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import main.Database.DatabaseConnector;
import main.Model.ReadData;
import main.Model.Robot;
import org.bson.Document;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

public class RobotDAO implements RobotDAO_Interface {
//    @Override
//    public void insert(Robot robot, Connection connection) {
//        if (connection == null)
//            connection = databaseConnector.getDatabase();
//
//        String query = "INSERT INTO robot (robot.id, clusterId, ir, count, downTime, startUpTime) VALUES (?,?,?,?,?,?); ";
//        try {
//            PreparedStatement statement = connection.prepareStatement(query);
//
//            statement.setString(1, robot.getRobotId());
//            statement.setString(2, robot.getClusterId());
//            statement.setFloat(3, robot.getInefficiencyRate());
//            statement.setInt(4, robot.getCountInefficiencyComponents());
//            statement.setInt(5, robot.getDownTime());
//            statement.setTimestamp(6, robot.getStartUpTime());
//
//            statement.execute();
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        databaseConnector.closeConnectionToDB(connection);
//    }
//
//    @Override
//    public Robot get(int robotId) {
//        return null;
//    }
//
//    @Override
//    public Robot findRobotByIdOrInsert(ReadData readData) {
//        Connection connection = databaseConnector.getConnection();
//        Robot robot = null;
//
//        String query = "SELECT * FROM robot WHERE robot.id = ? ;";
//        try {
//            PreparedStatement statement = connection.prepareStatement(query);
//
//            statement.setString(1, readData.getRobot());
//            ResultSet resultSet = statement.executeQuery();
//
//            int count = 0;
//
//            while (resultSet.next()) {
//                count++;
//                // There is already a robot in the DB.
//
//                robot = new Robot();
//
//                robot.setRobotId(readData.getRobot());
//                robot.setClusterId(resultSet.getString(Robot.CLUSTER_ID));
//                robot.setInefficiencyRate(resultSet.getFloat(Robot.INEFFICIENCY_RATE));
//                robot.setCountInefficiencyComponents(resultSet.getInt(Robot.COUNT_INEFFICIENCY_COMPONENTS));
//                robot.setDownTime(resultSet.getInt(Robot.DOWN_TIME));
//                robot.setStartDownTime(resultSet.getTimestamp(Robot.START_DOWN_TIME));
//                robot.setStartUpTime(resultSet.getTimestamp(Robot.START_UP_TIME));
//                // TODO add other meaningful attributes.
//            }
//
//            if (count == 0) {
////                System.out.println("src.test.Robot not found.");
//                // The cluster is not present, then we need to initialize it.
//                robot = new Robot();
//                robot.setRobotId(readData.getRobot());
//                robot.setClusterId(readData.getCluster());
//                robot.setInefficiencyRate((float) 0.0);
//                robot.setCountInefficiencyComponents(0);
//                robot.setDownTime(0);
//
//                Timestamp now = new Timestamp(System.currentTimeMillis());
//                if (now.after(readData.getTimestamp()))
//                    robot.setStartUpTime(readData.getTimestamp());
//                else
//                    robot.setStartUpTime(now);
//                // Also an entry in the table history has to be created.
//                new HistoryDAO().insertPeriodStart(robot.getRobotId(), robot.getStartUpTime(), true, 0);
//
//                this.insert(robot, connection);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        if (connection != null)
//            databaseConnector.closeConnectionToDB(connection);
//        return robot;
//    }
//
//    @Override
//    public void updateCountAndStartDown(Robot robot, ReadData readData) {
//        Connection connection = databaseConnector.getConnection();
//
//        String query = "UPDATE robot " +
//                "SET count=? , startDownTime=? " +
//                "WHERE id = ?;";
//
//        try {
//            PreparedStatement statement = connection.prepareStatement(query);
//
//            statement.setInt(1, robot.getCountInefficiencyComponents());
//            statement.setTimestamp(2, readData.getTimestamp());
//            statement.setString(3, robot.getRobotId());
//
//            statement.execute();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        databaseConnector.closeConnectionToDB(connection);
//    }
//
//    public void updateCountAndStopDown(Robot robot, ReadData readData, long downTimeDiff) {
//        Connection connection = databaseConnector.getConnection();
//
//        String query = "UPDATE robot " +
//                "SET count=? ,  startDownTime=? , downTime=? " +
//                "WHERE id=? ";
//        try {
//            PreparedStatement statement = connection.prepareStatement(query);
//
//            statement.setInt(1, robot.getCountInefficiencyComponents());
//            statement.setTimestamp(2, null);
//            statement.setLong(3, downTimeDiff);
//            statement.setString(4, robot.getRobotId());
//
//            statement.execute();
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        databaseConnector.closeConnectionToDB(connection);
//    }
//
//    public void updateCount(Robot robot) {
//        Connection connection = databaseConnector.getConnection();
//
//        String query = "UPDATE robot " +
//                "SET count = ? " +
//                "WHERE id=?; ";
//
//        try {
//            PreparedStatement statement = connection.prepareStatement(query);
//
//            statement.setInt(1, robot.getCountInefficiencyComponents());
//            statement.setString(2, robot.getRobotId());
//
//            statement.execute();
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        databaseConnector.closeConnectionToDB(connection);
//    }
//
//    @Override
//    public long getDownTime(String robotId) {
//        Connection connection = databaseConnector.getConnection();
//
//        String query = "SELECT downTime " +
//                "FROM robot " +
//                "WHERE id = ?;";
//        long downTime = 0;
//
//        try {
//            PreparedStatement statement = connection.prepareStatement(query);
//
//            statement.setString(1, robotId);
//            ResultSet resultSet = statement.executeQuery();
//
//            while (resultSet.next()) {
//                downTime = resultSet.getLong(Robot.DOWN_TIME);
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        databaseConnector.closeConnectionToDB(connection);
//        return downTime;
//    }
//
//    @Override
//    public void delete(String robotId) {
//        Connection connection = databaseConnector.getConnection();
//
//        String query = "DELETE FROM robot " +
//                "WHERE id=?;";
//
//        try {
//            PreparedStatement statement = connection.prepareStatement(query);
//            statement.setString(1, robotId);
//            statement.execute();
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        databaseConnector.closeConnectionToDB(connection);
//    }
//
////    @Override
////    public void processRobotIR() {
////        Connection connection = databaseConnector.getConnection();
////        Queue<src.test.Robot> queue = new LinkedList<src.test.Robot>();
////
////        String query = "SELECT id, downTime, startUpTime, startDownTime" +
////                " FROM robot;";
////
////        try {
////            PreparedStatement statement = connection.prepareStatement(query);
////            ResultSet resultSet = statement.executeQuery();
////
////            while (resultSet.next()) {
////                src.test.Robot robot = new src.test.Robot();
////
////                robot.setRobotId(resultSet.getString(src.test.Robot.ROBOT_ID));
////                robot.setDownTime(resultSet.getInt(src.test.Robot.DOWN_TIME));
////                robot.setStartUpTime(resultSet.getTimestamp(src.test.Robot.START_UP_TIME));
////                robot.setStartDownTime(resultSet.getTimestamp(src.test.Robot.START_DOWN_TIME));
////
////                queue.add(robot);
////            }
////        } catch (SQLException e) {
////            e.printStackTrace();
////        }
////
////        databaseConnector.closeConnectionToDB(connection);
////        calculateIR(queue);
////
////    }
//
//    @Override
//    public List<Robot> getAllRobots() {
//        Connection connection = databaseConnector.getConnection();
//        PreparedStatement preparedStatement = null;
//
//        String query = "SELECT id, clusterId, ir FROM robot ORDER BY id;";
//        LinkedList<Robot> robotLinkedList = new LinkedList<>();
//
//        try {
//            PreparedStatement statement = connection.prepareStatement(query);
//            ResultSet resultSet = statement.executeQuery();
//
//            while (resultSet.next()) {
//                robotLinkedList.add(
//                        new Robot(resultSet.getString(Robot.ROBOT_ID),
//                                resultSet.getString(Robot.CLUSTER_ID),
//                                resultSet.getFloat(Robot.INEFFICIENCY_RATE)));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        databaseConnector.closeConnectionToDB(connection);
//
//        return robotLinkedList;
//    }
//
//    // TODO Here
//    public HashMap<String, Robot> getAllRobotsMap() {
//        Connection connection = databaseConnector.getConnection();
//        PreparedStatement preparedStatement = null;
//
//        String query = "SELECT id, clusterId, ir FROM robot ORDER BY id;";
//
//        HashMap<String, Robot> map = new HashMap<>();
//
//        try {
//            PreparedStatement statement = connection.prepareStatement(query);
//            ResultSet resultSet = statement.executeQuery();
//
//            while (resultSet.next()) {
//                map.put(resultSet.getString(Robot.ROBOT_ID),
//                        new Robot(resultSet.getString(Robot.ROBOT_ID),
//                                resultSet.getString(Robot.CLUSTER_ID),
//                                resultSet.getFloat(Robot.INEFFICIENCY_RATE)));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        databaseConnector.closeConnectionToDB(connection);
//        return map;
//    }
//
//    @Override
//    public void updateIR(HashMap<String, Float> robotsIR) {
//        Connection connection = databaseConnector.getConnection();
//
//        PreparedStatement statement = null;
//
//        String query = "UPDATE robot" +
//                " SET ir = ? WHERE id = ?;";
//
//        try {
//            connection.setAutoCommit(false);
//            statement = connection.prepareStatement(query);
//
//            for (Map.Entry<String, Float> entry : robotsIR.entrySet()) {
//                statement.setFloat(1, entry.getValue());
//                statement.setString(2, entry.getKey());
//
//                statement.addBatch();
//
//            }
//
//            statement.executeBatch();
//            connection.commit();
//            System.out.println("Fatto!");
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        databaseConnector.closeConnectionToDB(connection);
//    }
//
//    //    private void calculateIR(Queue<src.test.Robot> queue) {
////        Timestamp now = new Timestamp(System.currentTimeMillis());
////        Connection connection = databaseConnector.getConnection();
////        PreparedStatement statement = null;
////
////        String query = "UPDATE robot" +
////                " SET ir = ? " +
////                " WHERE id = ?;";
////
////        try {
////            connection.setAutoCommit(false);
////            statement = connection.prepareStatement(query);
////
////            while (!queue.isEmpty()) {
////                src.test.Robot robot = queue.remove();
////
//////                long upTime = Util.differenceBetweenTimestamps(now, robot.getStartUpTime());
////                long downTime = robot.getDownTime();
////
////                // Is the robot still down?
////                if (robot.getStartDownTime() != null) {
////                    // src.test.Robot is still down.
////                    downTime += Util.differenceBetweenTimestamps(now, robot.getStartDownTime());
////                }
////
//////                float ir = (downTime / upTime) * 100;
////
////                // downTime is in second so I need to cast it in minutes.
////                downTime = downTime / 60;
////
////                // 60 is the temporal window.
////                float ir = ((float) downTime / 60) * 100;
////                ir = (float) (Math.round(ir * 100.0) / 100.0);
////                robot.setInefficiencyRate(ir);
////
////                statement.setFloat(1, robot.getInefficiencyRate());
////                statement.setString(2, robot.getRobotId());
////                statement.addBatch();
////            }
////
////            statement.executeBatch();
////            connection.commit();
////            System.out.println("Fatto!");
////        } catch (SQLException e) {
////            e.printStackTrace();
////        }
////        databaseConnector.closeConnectionToDB(connection);
////    }
//    public void populateWithRobots(HashMap<String, Cluster> clusters) {
//        Connection connection = databaseConnector.getConnection();
//
//        String query = "SELECT id, clusterId, ir " +
//                " FROM robot " +
//                " ORDER BY clusterId;";
//
//        // Il primo string è il cluster Id, il secondo è il robotId
////        HashMap<String, HashMap<String, Robot>> map;
//        try {
//            Statement statement = connection.createStatement();
//            ResultSet resultSet = statement.executeQuery(query);
//
//            while (resultSet.next()) {
//                Robot robot = new Robot(
//                        resultSet.getString(Robot.ROBOT_ID),
//                        resultSet.getString(Robot.CLUSTER_ID),
//                        resultSet.getFloat(Robot.INEFFICIENCY_RATE));
//                clusters.get(resultSet.getString(Robot.CLUSTER_ID)).addRobot(robot);
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public Robot callProcedure(ReadData readData) {
//        Connection connection = databaseConnector.getConnection();
//        CallableStatement cs = null;
//        Robot robot = null;
//
//        try {
//            cs = connection.prepareCall("{call robotSelectOrInsert(?,?,?,?)}");
//            cs.setString(1, readData.getRobot());
//            cs.setString(2, readData.getCluster());
//            cs.registerOutParameter(4, Types.INTEGER);
//
//            Timestamp now = new Timestamp(System.currentTimeMillis());
//            if (now.after(readData.getTimestamp()))
//                cs.setTimestamp(3, readData.getTimestamp());
//            else
//                cs.setTimestamp(3, now);
//
//            cs.execute();
//
//            int inserted = cs.getInt(4);
//            ResultSet resultSet = cs.getResultSet();
//
//            while (resultSet.next()) {
//                robot = new Robot();
//
//                robot.setRobotId(readData.getRobot());
//                robot.setClusterId(resultSet.getString(Robot.CLUSTER_ID));
//                robot.setInefficiencyRate(resultSet.getFloat(Robot.INEFFICIENCY_RATE));
//                robot.setCountInefficiencyComponents(resultSet.getInt(Robot.COUNT_INEFFICIENCY_COMPONENTS));
//                robot.setDownTime(resultSet.getInt(Robot.DOWN_TIME));
//                robot.setStartDownTime(resultSet.getTimestamp(Robot.START_DOWN_TIME));
//                robot.setStartUpTime(resultSet.getTimestamp(Robot.START_UP_TIME));
//            }
//
//            if (inserted == 0) {
//                new HistoryDAO().insertPeriodStart(robot.getClusterId(), robot.getStartUpTime(), true, 0);
//            }
//
//        } catch (SQLException e) {
//            System.err.println("SQLException: " + e.getMessage());
//        } finally {
//            databaseConnector.closeConnectionToDB(connection);
//        }
//        return robot;
//    }

    public HashMap<String, Robot> getRobotsForCluster(String clusterId) {

        MongoDatabase mongoDatabase = DatabaseConnector.getInstance().getMongoDatabase();
        MongoCollection<Document> robotsCollection = mongoDatabase.getCollection("robots");
        HashMap<String, Robot> robots = new HashMap<String, Robot>();

        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("clusterId", clusterId);

        MongoCursor<Document> cursor = robotsCollection.find(searchQuery).iterator();
        try {
            while (cursor.hasNext()) {
                Document item = cursor.next();

                Robot robot = new Robot();
                robot.setRobotId(item.getString(Robot.ROBOT_ID));
                robot.setClusterId(item.getString(Robot.CLUSTER_ID));
                robot.setStartUpTime(new Timestamp(item.getLong(Robot.START_UP_TIME)));
                robot.setInefficiencyRate(item.getDouble(Robot.INEFFICIENCY_RATE));
                robot.setCountInefficiencyComponents(item.getInteger(Robot.COUNT_INEFFICIENCY_COMPONENTS));

                robots.put(robot.getRobotId(), robot);
            }
        } finally {
            cursor.close();
        }
        cursor.close();
        return robots;

    }

    @Override
    public void insert(ReadData readData) {
        MongoDatabase mongoDatabase = DatabaseConnector.getInstance().getMongoDatabase();
        MongoCollection<Document> robots = mongoDatabase.getCollection("robots");

        Timestamp now = new Timestamp(System.currentTimeMillis());
        Document newRobot = new Document()
                .append(Robot.ROBOT_ID, readData.getRobot())
                .append(Robot.CLUSTER_ID, readData.getCluster())
                .append(Robot.INEFFICIENCY_RATE, 0.0)
                .append(Robot.COUNT_INEFFICIENCY_COMPONENTS, 0)
                .append(Robot.START_UP_TIME, (now.after(readData.getTimestamp()) ? readData.getTimestamp().toString() : now.toString()))
                .append(Robot.START_DOWN_TIME, null)
                .append(Robot.DOWN_TIME, 0);
        robots.insertOne(newRobot);
    }

    @Override
    public Robot get(int robotId) {
        return null;
    }

    @Override
    public Robot findRobotByIdOrInsert(ReadData readData) {
        return null;
    }

    @Override
    public void updateCountAndStartDown(Robot robot, ReadData readData) {
        MongoDatabase mongoDatabase = DatabaseConnector.getInstance().getMongoDatabase();
        MongoCollection<Document> robots = mongoDatabase.getCollection("robots");


        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("_id", robot.getRobotId());

        BasicDBObject updateFields = new BasicDBObject();
        updateFields.append(Robot.COUNT_INEFFICIENCY_COMPONENTS, robot.getCountInefficiencyComponents());
        updateFields.append(Robot.START_DOWN_TIME, readData.getTimestamp().getTime());
        BasicDBObject setQuery = new BasicDBObject();
        setQuery.append("$set", updateFields);

        robots.updateOne(searchQuery, setQuery);
    }

    @Override
    public void updateCountAndStopDown(Robot robot, ReadData readData, long downTimeDiff) {
        MongoDatabase mongoDatabase = DatabaseConnector.getInstance().getMongoDatabase();
        MongoCollection<Document> robots = mongoDatabase.getCollection("robots");

        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("_id", robot.getRobotId());

        BasicDBObject updateFields = new BasicDBObject();
        updateFields.append(Robot.COUNT_INEFFICIENCY_COMPONENTS, robot.getCountInefficiencyComponents());
        updateFields.append(Robot.START_DOWN_TIME, null);
        BasicDBObject setQuery = new BasicDBObject();
        setQuery.append("$set", updateFields);
        setQuery.append("$inc", new BasicDBObject(Robot.DOWN_TIME, downTimeDiff));

        robots.updateOne(searchQuery, setQuery);
    }

    @Override
    public void updateCount(Robot robot) {
        MongoDatabase mongoDatabase = DatabaseConnector.getInstance().getMongoDatabase();
        MongoCollection<Document> robots = mongoDatabase.getCollection("robots");

        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("_id", robot.getRobotId());

        BasicDBObject updateFields = new BasicDBObject();
        updateFields.append(Robot.COUNT_INEFFICIENCY_COMPONENTS, robot.getCountInefficiencyComponents());
        BasicDBObject setQuery = new BasicDBObject();
        setQuery.append("$set", updateFields);

        robots.updateOne(searchQuery, setQuery);
    }

    @Override
    public long getDownTime(String robotId) {
        return 0;
    }

    @Override
    public void delete(String p3Z) {

    }

    @Override
    public List<Robot> getAllRobots() {
        return null;
    }

    @Override
    public void updateIR(HashMap<String, Float> robotsIR) {

    }

    public Robot getRobot(ReadData readData) {
        MongoDatabase mongoDatabase = DatabaseConnector.getInstance().getMongoDatabase();
        MongoCollection<Document> robotsCollection = mongoDatabase.getCollection("robots");

        Robot robot = null;

        // Where clause of the query.
        Document whereQuery = new Document("_id", readData.getRobot());

        // Item to insert if no cluster is already present.
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Document setOnInsert = new Document()
                .append(Robot.ROBOT_ID, readData.getRobot())
                .append(Robot.CLUSTER_ID, readData.getCluster())
                .append(Robot.INEFFICIENCY_RATE, 0.0)
                .append(Robot.COUNT_INEFFICIENCY_COMPONENTS, 0)
                .append(Robot.START_UP_TIME, (now.after(readData.getTimestamp()) ? readData.getTimestamp().getTime() : now.getTime()))
                .append(Robot.START_DOWN_TIME, null)
                .append(Robot.DOWN_TIME, 0);
        Document update = new Document("$setOnInsert", setOnInsert);

        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions();
        options.returnDocument(ReturnDocument.AFTER);
        options.upsert(true);

        Document document = robotsCollection.findOneAndUpdate(whereQuery, update, options);
        robot = new Robot(document);
        return robot;
    }
}
