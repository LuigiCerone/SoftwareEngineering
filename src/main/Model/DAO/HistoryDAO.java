package main.Model.DAO;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import main.Database.DatabaseConnector;
import main.Model.Cluster;
import main.Model.History;
import main.Model.Robot;
import org.bson.Document;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;

public class HistoryDAO implements HistoryDAO_Interface {
    @Override
    public void insertPeriodStart(String deviceId, Timestamp start, boolean status, int type) {

    }

    @Override
    public void insertPeriodEnd(String deviceId, Timestamp end, boolean status, int type) {
        MongoDatabase mongoDatabase = DatabaseConnector.getInstance().getMongoDatabase();
        MongoCollection<Document> collection = null;

        try {
            String KEY = null;
            if (type == 0) {
                collection = mongoDatabase.getCollection("robots");
                KEY = Robot.HISTORIES + ".$." + History.END;
            } else if (type == 1) {
                collection = mongoDatabase.getCollection("clusters");
                KEY = Cluster.HISTORIES + ".$." + History.END;
            }

            // Where clause of the query.
            Document whereQuery = new Document();
            whereQuery.append("_id", deviceId);
            whereQuery.append(KEY, new Document("$eq", null));

            // Update clause of the query.
            BasicDBObject updateFields = new BasicDBObject();
            updateFields.append(KEY, end.getTime());
            BasicDBObject setQuery = new BasicDBObject();
            setQuery.append("$set", updateFields);

            UpdateResult document = collection.updateOne(whereQuery, setQuery);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public HashMap<String, HashSet<History>> processIR(Timestamp now, Timestamp oneHourAgo, int type) {
        return null;
    }
//    private DatabaseConnector databaseConnector;
//
//    public HistoryDAO() {
//        this.databaseConnector = new DatabaseConnector();
//    }
//
//    @Override
//    public void insertPeriodStart(String deviceId, Timestamp start, boolean status, int type) {
//        Connection connection = databaseConnector.getConnection();
//
//        String query = "INSERT INTO history (deviceId, startTime, status, type) " +
//                "VALUES (?,?,?,?);";
//
//        try {
//            PreparedStatement statement = connection.prepareStatement(query);
//
//            statement.setString(1, deviceId);
//            statement.setTimestamp(2, start);
//            statement.setBoolean(3, status);
//            statement.setInt(4, type);
//
//            statement.execute();
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        databaseConnector.closeConnectionToDB(connection);
//    }
//
//    // type=0 means src.test.Robot, type=1 means Cluster.
//    @Override
//    public void insertPeriodEnd(String deviceId, Timestamp end, boolean status, int type) {
//        Connection connection = databaseConnector.getConnection();
//
//        String query = "UPDATE history " +
//                "SET endTime=? " +
//                "WHERE deviceId = ? AND status=? AND endTime IS NULL AND startTime IS NOT NULL AND type=?;";
//
//        try {
//            PreparedStatement statement = connection.prepareStatement(query);
//
//            statement.setTimestamp(1, end);
//            statement.setString(2, deviceId);
//            statement.setBoolean(3, status);
//            statement.setInt(4, type);
//
//            statement.execute();
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        databaseConnector.closeConnectionToDB(connection);
//    }
//
//    // If type = 1 then clusters' IR will be calculated, else if type = 0 robots' IR will be.
//    @Override
//    public HashMap<String, HashSet<History>> processIR(Timestamp now, Timestamp oneHourAgo, int type) {
//        Connection connection = databaseConnector.getConnection();
//
//        String query = "SELECT * FROM history " +
//                "WHERE type=? AND (startTime>= ? OR endTime>=? OR endTime IS NULL)  " +
//                "ORDER BY deviceId;";
//
//        HashMap<String, HashSet<History>> map = new HashMap<>();
//
//        try {
//            PreparedStatement preparedStatement = connection.prepareStatement(query);
//            preparedStatement.setInt(1, type);
//            preparedStatement.setTimestamp(2, oneHourAgo);
//            preparedStatement.setTimestamp(3, oneHourAgo);
//
//
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            while (resultSet.next()) {
//                String deviceId = resultSet.getString(History.DEVICE_ID);
//
//                if (!map.containsKey(deviceId)) {
//                    map.put(deviceId, new HashSet<History>());
//                }
//                HashSet<History> histories = map.get(deviceId);
//
//                History history = new History();
//                history.setId(resultSet.getInt(History.HISTORY_ID));
//                history.setDeviceId(deviceId);
//
////                if (resultSet.getTimestamp(History.START).before(oneHourAgo))
////                    history.setStart(null);
////                else
////                    history.setStart(resultSet.getTimestamp(History.START));
//                history.setStart(resultSet.getTimestamp(History.START));
//
//                history.setEnd(resultSet.getTimestamp(History.END));
//                history.setStatus(resultSet.getBoolean(History.STATUS));
//
//                histories.add(history);
//
//                map.put(deviceId, histories); // Update. Maybe this isn't needed.
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        databaseConnector.closeConnectionToDB(connection);
//        return map;
//    }
}
