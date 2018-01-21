package main.Model.DAO;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import main.Database.DatabaseConnector;
import main.Model.Cluster;
import main.Model.History;
import main.Model.Robot;
import org.bson.Document;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;

public class HistoryDAO implements HistoryDAO_Interface {
    @Override
    public void insertPeriodStart(String deviceId, Timestamp start, boolean status, int type) {
        MongoDatabase mongoDatabase = DatabaseConnector.getInstance().getMongoDatabase();
        MongoCollection<Document> collection = null;

        try {
            String KEY = null;
            if (type == 0) {
                collection = mongoDatabase.getCollection("robots");
                KEY = Robot.HISTORIES;
            } else if (type == 1) {
                collection = mongoDatabase.getCollection("clusters");
                KEY = Cluster.HISTORIES;
            }

            // Where clause of the query.
            Document whereQuery = new Document();
            whereQuery.append("_id", deviceId);

            Document historyDoc = new History(deviceId, start, status).toDocument();
            BasicDBObject updateQuery = new BasicDBObject("$push", new BasicDBObject(KEY, historyDoc));
            collection.updateOne(whereQuery, updateQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insertPeriodEnd(String deviceId, Timestamp end, boolean status, int type) {
        MongoDatabase mongoDatabase = DatabaseConnector.getInstance().getMongoDatabase();
        MongoCollection<Document> collection = null;

        try {
            String KEY = null;
            if (type == 0) {
                collection = mongoDatabase.getCollection("robots");
                KEY = Robot.HISTORIES;
            } else if (type == 1) {
                collection = mongoDatabase.getCollection("clusters");
                KEY = Cluster.HISTORIES;
            }

            // Where clause of the query.
            Document whereQuery = new Document();
            whereQuery.append("_id", deviceId);
            whereQuery.append(KEY + "." + History.END, null);

            // Update clause of the query.
            BasicDBObject updateFields = new BasicDBObject();
            updateFields.append(KEY + ".$." + History.END, end.getTime());
            BasicDBObject setQuery = new BasicDBObject();
            setQuery.append("$set", updateFields);

            collection.updateOne(whereQuery, setQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void oldHistoriesCleaner() {
        MongoDatabase mongoDatabase = DatabaseConnector.getInstance().getMongoDatabase();
        MongoCollection<Document> collection = mongoDatabase.getCollection("robots");

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -90);
        long nintyMinutesAgo = calendar.getTimeInMillis();

//         Shell Query that seems to work.
//        db.getCollection('robots').updateMany({},
//                {$pull:{'histories':{'endTime': {$lt:timestamp}}}},
//        {multi:true})

        Document whereQuery = new Document();
        whereQuery.append("$lt", nintyMinutesAgo);
        Document fields = new Document(Robot.HISTORIES, new Document((History.END), whereQuery));
        Document pullQuery = new Document("$pull", fields);
        collection.updateMany(new Document(), pullQuery);
//
        collection = mongoDatabase.getCollection("clusters");
        collection.updateMany(new Document(), pullQuery);
    }
}
