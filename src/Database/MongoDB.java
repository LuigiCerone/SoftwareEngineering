package Database;

import Model.ReadData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.*;
import com.mongodb.client.MongoCursor;
import com.mongodb.util.JSON;
import de.undercouch.bson4jackson.BsonFactory;
import org.jongo.Jongo;
import org.jongo.MongoCollection;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class MongoDB {
    public static final String DATABASE_NAME = "SEProject";
    public static final String DATAREADINGS_COLLECTION = "datareadings";

    private DB mongoDatabase = this.getDatabase();
    private Jongo jongo = null;
    private MongoCollection robotsCollection = this.setDefaultCollection();

    public MongoDB() {
    }

    public DB getDatabase() {
        return MongoClientSingleton.getInstance().getDB(DATABASE_NAME);
    }

    public MongoCollection setDefaultCollection() {
        this.jongo = new Jongo(mongoDatabase);
        return jongo.getCollection(DATAREADINGS_COLLECTION);
    }

    public void getAllRobotsData() {
        // MongoCursor<ReadData> cursor = robotsCollection.find().as(ReadData.class);
        ReadData r = robotsCollection.findOne().as(ReadData.class);
        System.out.println(r.toString());
//        while (cursor.hasNext()) {
//            System.out.println(cursor.next());
//        }
    }

    public void insertRobotData(String data) throws IOException {
        // DBObject dbObject = (DBObject) JSON.parse(data);
        //robotsCollection.insert(data);
        ReadData rd = new ObjectMapper().readerFor(ReadData.class).readValue(data);
        System.out.println(new ObjectMapper().writeValueAsString(rd));
    }

    // Assumption, in our database empty collections don't exists.
//    public DBCollection checkForClusterCollectionOrCreateIt(String clusterId) {
//        DBCollection table = mongoDatabase.getCollection(clusterId);
//        return table;
//        if(table.count() > 0){
//            return table;
//        }
//        else{
//            DBCollection collection = mongoDatabase.createCollection(clusterId, null);
//            return collection;
//        }
//        boolean collectionExists = mongoDatabase.collectionExists(clusterId);
//        DBCollection collection = null;
//        if (!collectionExists) {
//            collection = mongoDatabase.createCollection(clusterId, new BasicDBObject("capped", false));
//        }
//        return collection;
//    }
//
//    public void checkForRobotDocumentOrCreateIt(DBCollection collection, JSONObject json) {
//        BasicDBObject whereQuery = new BasicDBObject();
//        whereQuery.put("_id", json.getJSONObject("robot").getString("_id"));
//
//        DBCursor cursor = collection.find();
//        while (cursor.hasNext()) {
//            System.out.println(cursor.next());
//        }
//
//        cursor = collection.find(whereQuery);
//        while (cursor.hasNext()) {
//            System.out.println(cursor.next());
//        }
//
//}
//
//    public void insertRobotInCluster(JsonObject json) {
//        JsonObject jsonObject = json.getAsJsonObject("robot");
//        String clusterId = jsonObject.get("_cluster").getAsString();
//        String robotId = jsonObject.get("_id").getAsString();
//
//        mongoDatabase.getCollection(clusterId).insert(json);
//
//    }
}
