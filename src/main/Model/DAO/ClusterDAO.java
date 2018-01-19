package main.Model.DAO;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import main.Database.DatabaseConnector;
import main.Model.Cluster;
import main.Model.History;
import main.Model.ReadData;
import main.Model.Robot;
import org.bson.Document;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class ClusterDAO implements ClusterDAO_Interface {

//    @Override
//    public void insert(Cluster cluster, Connection connection) {
//        if (connection == null)
//            connection = databaseConnector.getConnection();
//
//        String query = "INSERT INTO cluster (id, zoneId, ir, count, downTime, startUpTime) VALUES (?,?,?,?,?,?);";
//        try {
//            PreparedStatement statement = connection.prepareStatement(query);
//            statement.setString(1, cluster.getClusterId());
//            statement.setString(2, cluster.getZoneId());
//            statement.setFloat(3, cluster.getInefficiencyRate());
//            statement.setInt(4, cluster.getCountInefficiencyComponents());
//            statement.setLong(5, cluster.getDownTime());
//            statement.setTimestamp(6, cluster.getStartUpTime());
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
//    public Cluster findClusterByIdOrInsert(ReadData readData) {
//        Connection connection = databaseConnector.getConnection();
//        Cluster cluster = null;
//
//        String query = "SELECT * FROM cluster WHERE cluster.id = ? ;";
//        try {
//            PreparedStatement statement = connection.prepareStatement(query);
//
//            statement.setString(1, readData.getCluster());
//            ResultSet resultSet = statement.executeQuery();
//
//            int count = 0;
//
//            while (resultSet.next()) {
//                count++;
//                cluster = new Cluster();
//
//                cluster.setClusterId(readData.getCluster());
//                cluster.setZoneId(resultSet.getString(Cluster.CLUSTER_ID));
//                cluster.setInefficiencyRate(resultSet.getFloat(Cluster.INEFFICIENCY_RATE));
//                cluster.setCountInefficiencyComponents(resultSet.getInt(Cluster.COUNT_INEFFICIENCY_COMPONENTS));
//                cluster.setDownTime(resultSet.getInt(Cluster.DOWN_TIME));
//                cluster.setStartDownTime(resultSet.getTimestamp(Cluster.START_DOWN_TIME));
//                cluster.setStartUpTime(resultSet.getTimestamp(Cluster.START_UP_TIME));
//                // TODO add other meaningful attributes.
//            }
//
//            if (count == 0) {
////                System.out.println("Cluster not found.");
//                // The cluster is not present, then we need to initialize it.
//                cluster = new Cluster();
//                cluster.setClusterId(readData.getCluster());
//                cluster.setZoneId(readData.getZone());
//                cluster.setInefficiencyRate((float) 0.0);
//                cluster.setCountInefficiencyComponents(0);
//                cluster.setDownTime(0);
//
//                Timestamp now = new Timestamp(System.currentTimeMillis());
//                if (now.after(readData.getTimestamp()))
//                    cluster.setStartUpTime(readData.getTimestamp());
//                else
//                    cluster.setStartUpTime(now);
//
//                this.insert(cluster, connection);
//
//                // Also an entry in the table history has to be created.
//                new HistoryDAO().insertPeriodStart(cluster.getClusterId(), cluster.getStartUpTime(), true, 1);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        if (connection != null)
//            databaseConnector.closeConnectionToDB(connection);
//        return cluster;
//    }
//
//    public Cluster callProcedure(ReadData readData) {
//        Connection connection = databaseConnector.getConnection();
//        CallableStatement cs = null;
//        Cluster cluster = null;
//        ResultSet resultSet = null;
//
//        try {
//            cs = connection.prepareCall("{call clusterSelectOrInsert(?,?,?,?)}");
//            cs.setString(1, readData.getCluster());
//            cs.setString(2, readData.getZone());
//            cs.registerOutParameter(4, java.sql.Types.INTEGER);
//            Timestamp now = new Timestamp(System.currentTimeMillis());
//            if (now.after(readData.getTimestamp()))
//                cs.setTimestamp(3, readData.getTimestamp());
//            else
//                cs.setTimestamp(3, now);
//            cs.execute();
//            resultSet = cs.getResultSet();
//
//            int inserted = cs.getInt(4);
////            System.out.println(inserted);
//
//            while (resultSet.next()) {
//                cluster = new Cluster();
//
//                cluster.setClusterId(readData.getCluster());
//                cluster.setZoneId(resultSet.getString(Cluster.CLUSTER_ID));
//                cluster.setInefficiencyRate(resultSet.getFloat(Cluster.INEFFICIENCY_RATE));
//                cluster.setCountInefficiencyComponents(resultSet.getInt(Cluster.COUNT_INEFFICIENCY_COMPONENTS));
//                cluster.setDownTime(resultSet.getInt(Cluster.DOWN_TIME));
//                cluster.setStartDownTime(resultSet.getTimestamp(Cluster.START_DOWN_TIME));
//                cluster.setStartUpTime(resultSet.getTimestamp(Cluster.START_UP_TIME));
//            }
//            if (inserted == 0) {
//                new HistoryDAO().insertPeriodStart(cluster.getClusterId(), cluster.getStartUpTime(), true, 1);
//            }
//        } catch (SQLException e) {
//            System.err.println("SQLException: " + e.getMessage());
//        } finally {
//            try {
//                if (resultSet != null) resultSet.close();
//            } catch (Exception e) {
//            }
//            ;
//            try {
//                if (cs != null) cs.close();
//            } catch (Exception e) {
//            }
//            ;
//            try {
//                if (connection != null) connection.close();
//            } catch (Exception e) {
//            }
//            ;
//            databaseConnector.closeConnectionToDB(connection);
//        }
//        return cluster;
//    }
//
//    @Override
//    public void updateCountAndStartDown(Cluster cluster, ReadData readData) {
//        Connection connection = databaseConnector.getConnection();
//
//        String query = "UPDATE cluster " +
//                "SET count=? , startDownTime=? " +
//                "WHERE id = ?;";
//        PreparedStatement statement = null;
//        try {
//            statement = connection.prepareStatement(query);
//
//            statement.setInt(1, cluster.getCountInefficiencyComponents());
//            statement.setTimestamp(2, readData.getTimestamp());
//            statement.setString(3, cluster.getClusterId());
//
//            statement.execute();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (statement != null) statement.close();
//            } catch (Exception e) {
//            }
//            ;
//            try {
//                if (connection != null) connection.close();
//            } catch (Exception e) {
//            }
//            ;
//            databaseConnector.closeConnectionToDB(connection);
//        }
//
//    }
//
//    @Override
//    public void updateCountAndStopDown(Cluster cluster, ReadData readData, long downTimeDiffCluster) {
//        Connection connection = databaseConnector.getConnection();
//
//        String query = "UPDATE cluster " +
//                "SET count=? ,  startDownTime=? , downTime=? " +
//                "WHERE id=? ";
//        PreparedStatement statement = null;
//        try {
//            statement = connection.prepareStatement(query);
//
//            statement.setInt(1, cluster.getCountInefficiencyComponents());
//            statement.setTimestamp(2, null);
//            statement.setLong(3, downTimeDiffCluster);
//            statement.setString(4, cluster.getClusterId());
//
//            statement.execute();
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (statement != null) statement.close();
//            } catch (Exception e) {
//            }
//            ;
//            try {
//                if (connection != null) connection.close();
//            } catch (Exception e) {
//            }
//            ;
//            databaseConnector.closeConnectionToDB(connection);
//        }
//    }
//
//    @Override
//    public void processClusterIR() {
//        Connection connection = databaseConnector.getConnection();
//        Queue<Cluster> queue = new LinkedList<Cluster>();
//
//        String query = "SELECT id, downTime, startUpTime, startDownTime" +
//                " FROM cluster;";
//
//        PreparedStatement statement = null;
//        ResultSet resultSet = null;
//        try {
//            statement = connection.prepareStatement(query);
//            resultSet = statement.executeQuery();
//
//            while (resultSet.next()) {
//                Cluster cluster = new Cluster();
//
//                cluster.setClusterId(resultSet.getString(Cluster.CLUSTER_ID));
//                cluster.setDownTime(resultSet.getInt(Cluster.DOWN_TIME));
//                cluster.setStartUpTime(resultSet.getTimestamp(Cluster.START_UP_TIME));
//                cluster.setStartDownTime(resultSet.getTimestamp(Cluster.START_DOWN_TIME));
//
//                queue.add(cluster);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (resultSet != null) resultSet.close();
//            } catch (Exception e) {
//            }
//            ;
//            try {
//                if (statement != null) statement.close();
//            } catch (Exception e) {
//            }
//            ;
//            try {
//                if (connection != null) connection.close();
//            } catch (Exception e) {
//            }
//            ;
//            databaseConnector.closeConnectionToDB(connection);
//        }
//        calculateIR(queue);
//    }
//
//    private void calculateIR(Queue<Cluster> queue) {
//
//        Timestamp now = new Timestamp(System.currentTimeMillis());
//        Connection connection = databaseConnector.getConnection();
//        PreparedStatement statement = null;
//
//        String query = "UPDATE cluster" +
//                " SET ir = ? " +
//                " WHERE id = ?;";
//
//        try {
//            connection.setAutoCommit(false);
//            statement = connection.prepareStatement(query);
//
//            while (!queue.isEmpty()) {
//                Cluster cluster = queue.remove();
//
////                long upTime = Util.differenceBetweenTimestamps(now, robot.getStartUpTime());
//                long downTime = cluster.getDownTime();
//
//                // Is the robot still down?
//                if (cluster.getStartDownTime() != null) {
//                    // src.test.Robot is still down.
//                    downTime += Util.differenceBetweenTimestamps(now, cluster.getStartDownTime());
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
//                cluster.setInefficiencyRate(ir);
//
//                statement.setFloat(1, cluster.getInefficiencyRate());
//                statement.setString(2, cluster.getClusterId());
//                statement.addBatch();
//            }
//
//            statement.executeBatch();
//            connection.commit();
//            System.out.println("Fatto!");
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (statement != null) statement.close();
//            } catch (Exception e) {
//            }
//            ;
//            try {
//                if (connection != null) connection.close();
//            } catch (Exception e) {
//            }
//            ;
//            databaseConnector.closeConnectionToDB(connection);
//        }
//    }
//
//    @Override
//    public void updateIR(HashMap<String, Float> clustersIR) {
//        Connection connection = databaseConnector.getConnection();
//
//        PreparedStatement statement = null;
//
//        String query = "UPDATE cluster" +
//                " SET ir = ? WHERE id = ?;";
//
//        try {
//            connection.setAutoCommit(false);
//            statement = connection.prepareStatement(query);
//
//            for (Map.Entry<String, Float> entry : clustersIR.entrySet()) {
//                statement.setFloat(1, entry.getValue());
//                statement.setString(2, entry.getKey());
//                statement.addBatch();
//            }
//            statement.executeBatch();
//            connection.commit();
//            System.out.println("Fatto!");
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (statement != null) statement.close();
//            } catch (Exception e) {
//            }
//            ;
//            try {
//                if (connection != null) connection.close();
//            } catch (Exception e) {
//            }
//            ;
//            databaseConnector.closeConnectionToDB(connection);
//        }
//    }
//
//    @Override
//    public LinkedList<Cluster> getAllClusters() {
//        Connection connection = databaseConnector.getConnection();
//        PreparedStatement preparedStatement = null;
//
//        String query = "SELECT id, zoneId, ir FROM cluster ORDER BY id;";
//        LinkedList<Cluster> clusterLinkedList = new LinkedList<>();
//        PreparedStatement statement = null;
//        ResultSet resultSet = null;
//        try {
//            statement = connection.prepareStatement(query);
//            resultSet = statement.executeQuery();
//
//            while (resultSet.next()) {
//                clusterLinkedList.add(
//                        new Cluster(
//                                resultSet.getString(Cluster.CLUSTER_ID),
//                                resultSet.getString(Cluster.ZONE_ID),
//                                resultSet.getFloat(Cluster.INEFFICIENCY_RATE)));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (statement != null) statement.close();
//            } catch (Exception e) {
//            }
//            ;
//            try {
//                if (resultSet != null) resultSet.close();
//            } catch (Exception e) {
//            }
//            ;
//            try {
//                if (connection != null) connection.close();
//            } catch (Exception e) {
//            }
//            ;
//            databaseConnector.closeConnectionToDB(connection);
//        }
//        return clusterLinkedList;
//    }
//
//
//    public HashMap<String, Cluster> getAllClusterMap() {
//        Connection connection = databaseConnector.getConnection();
//
//        String query = "SELECT id, zoneId, ir FROM cluster ORDER BY id;";
//        HashMap<String, Cluster> clusters = new HashMap<>();
//
//        Statement statement = null;
//        ResultSet resultSet = null;
//        try {
//            statement = connection.createStatement();
//            resultSet = statement.executeQuery(query);
//
//            while (resultSet.next()) {
//                clusters.put(resultSet.getString(Cluster.CLUSTER_ID),
//                        new Cluster(
//                                resultSet.getString(Cluster.CLUSTER_ID),
//                                resultSet.getString(Cluster.ZONE_ID),
//                                resultSet.getFloat(Cluster.INEFFICIENCY_RATE)));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (statement != null) statement.close();
//            } catch (Exception e) {
//            }
//            ;
//            try {
//                if (resultSet != null) resultSet.close();
//            } catch (Exception e) {
//            }
//            ;
//            try {
//                if (connection != null) connection.close();
//            } catch (Exception e) {
//            }
//            ;
//            databaseConnector.closeConnectionToDB(connection);
//        }
//
//        return clusters;
//    }


    public Cluster getCluster(ReadData readData) {
        MongoDatabase mongoDatabase = DatabaseConnector.getInstance().getMongoDatabase();
        MongoCollection<Document> clustersCollection = mongoDatabase.getCollection("clusters");

        Cluster cluster = null;

        // Where clause of the query.
        Document whereQuery = new Document("_id", readData.getCluster());

        // Item to insert if no cluster is already present.
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Timestamp startUp = (now.after(readData.getTimestamp()) ? readData.getTimestamp() : now);

        HashSet<Document> historiesAsDoc = new HashSet<>();
        historiesAsDoc.add(new History(readData, startUp, 1).toDocument());

        Document setOnInsert = new Document()
                .append(Cluster.CLUSTER_ID, readData.getCluster())
                .append(Cluster.ZONE_ID, readData.getZone())
                .append(Cluster.INEFFICIENCY_RATE, 0.0)
                .append(Cluster.COUNT_INEFFICIENCY_COMPONENTS, 0)
                .append(Cluster.START_UP_TIME, startUp.getTime())
                .append(Cluster.START_DOWN_TIME, null)
                .append(Cluster.DOWN_TIME, 0)
                .append(Cluster.HISTORIES, historiesAsDoc);
        Document update = new Document("$setOnInsert", setOnInsert);

        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions();
        options.returnDocument(ReturnDocument.AFTER);
        options.upsert(true);

        Document document = clustersCollection.findOneAndUpdate(whereQuery, update, options);
        cluster = new Cluster(document);

        return cluster;
    }

    public HashMap<String, Cluster> getClustersForZone(String zoneId) {
        MongoDatabase mongoDatabase = DatabaseConnector.getInstance().getMongoDatabase();
        MongoCollection<Document> clustersCollection = mongoDatabase.getCollection("clusters");

        HashMap<String, Cluster> clusters = new HashMap<String, Cluster>();

        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("zoneId", zoneId);

        MongoCursor<Document> cursor = clustersCollection.find(searchQuery).iterator();
        try {
            while (cursor.hasNext()) {
                Document item = cursor.next();

                Cluster cluster = new Cluster();
                cluster.setClusterId(item.getString(Cluster.CLUSTER_ID));
                cluster.setZoneId(item.getString(Cluster.ZONE_ID));
                cluster.setStartUpTime(new Timestamp(item.getLong(Cluster.START_UP_TIME)));
                cluster.setInefficiencyRate(item.getDouble(Cluster.INEFFICIENCY_RATE));
                cluster.setCountInefficiencyComponents(item.getInteger(Cluster.COUNT_INEFFICIENCY_COMPONENTS));

                // We need to get the robots in this cluster.
                HashMap<String, Robot> robots = new RobotDAO().getRobotsForCluster(cluster.getClusterId());
                cluster.setRobotsList(robots);
                clusters.put(cluster.getClusterId(), cluster);
            }
        } finally {
            cursor.close();
        }
        cursor.close();
        return clusters;
    }

    @Override
    public void insert(ReadData readData) {
        MongoDatabase mongoDatabase = DatabaseConnector.getInstance().getMongoDatabase();
        MongoCollection<Document> clusters = mongoDatabase.getCollection("clusters");

        Timestamp now = new Timestamp(System.currentTimeMillis());
        Document newCluster = new Document()
                .append(Cluster.CLUSTER_ID, readData.getCluster())
                .append(Cluster.ZONE_ID, readData.getZone())
                .append(Cluster.INEFFICIENCY_RATE, 0.0)
                .append(Cluster.COUNT_INEFFICIENCY_COMPONENTS, 0)
                .append(Cluster.START_UP_TIME, (now.after(readData.getTimestamp()) ? readData.getTimestamp().toString() : now.toString()))
                .append(Cluster.START_DOWN_TIME, null)
                .append(Cluster.DOWN_TIME, 0);
        clusters.insertOne(newCluster);
    }


    @Override
    public Cluster findClusterByIdOrInsert(ReadData readData) {
        return null;
    }

    @Override
    public void updateCountAndStartDown(Cluster cluster, ReadData readData) {
        MongoDatabase mongoDatabase = DatabaseConnector.getInstance().getMongoDatabase();
        MongoCollection<Document> clusters = mongoDatabase.getCollection("clusters");

        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put(Cluster.CLUSTER_ID, cluster.getClusterId());

        BasicDBObject updateFields = new BasicDBObject();
        updateFields.append(Cluster.COUNT_INEFFICIENCY_COMPONENTS, cluster.getCountInefficiencyComponents());
        updateFields.append(Cluster.START_DOWN_TIME, readData.getTimestamp().getTime());
        BasicDBObject setQuery = new BasicDBObject();
        setQuery.append("$set", updateFields);

        clusters.updateOne(searchQuery, setQuery);
    }

    @Override
    public void updateCountAndStopDown(Cluster cluster, ReadData readData, long downTimeDiffCluster) {
        MongoDatabase database = DatabaseConnector.getInstance().getMongoDatabase();
        MongoCollection<Document> clusters = database.getCollection("clusters");

        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put(Cluster.CLUSTER_ID, cluster.getClusterId());

        BasicDBObject updateFields = new BasicDBObject();
        updateFields.append(Cluster.COUNT_INEFFICIENCY_COMPONENTS, cluster.getCountInefficiencyComponents());
        updateFields.append(Cluster.START_DOWN_TIME, null);
        BasicDBObject setQuery = new BasicDBObject();
        setQuery.append("$set", updateFields);
        setQuery.append("$inc", new BasicDBObject(Cluster.DOWN_TIME, downTimeDiffCluster));

        clusters.updateOne(searchQuery, setQuery);
    }

    @Override
    public void processClusterIR() {

    }

    @Override
    public void updateIR(HashMap<String, Float> clustersIR) {

    }

    @Override
    public LinkedList<Cluster> getAllClusters() {
        return null;
    }
}
