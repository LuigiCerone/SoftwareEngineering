package Database;

import Model.ReadData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.io.IOException;


public class MongoDB {
    public static final String DATABASE_NAME = "SEProject";
    public static final String DATAREADINGS_COLLECTION = "datareadings";

    private MongoDatabase mongoDatabase = this.getDatabase();
    private MongoCollection<ReadData> robotsCollection = this.setDefaultCollection();

    public MongoDB() {
    }

    public MongoDatabase getDatabase() {
        return MongoClientSingleton.getInstance().getDatabase(DATABASE_NAME);
    }

    public MongoCollection<ReadData> setDefaultCollection() {
        return mongoDatabase.getCollection(DATAREADINGS_COLLECTION, ReadData.class);
    }

    public void getAllRobotsData() {
        // MongoCursor<ReadData> cursor = robotsCollection.find().as(ReadData.class);
//        ReadData r = robotsCollection.findOne().as(ReadData.class);
//        System.out.println(r.toString());
//        while (cursor.hasNext()) {
//            System.out.println(cursor.next());
//        }
    }

    public void insertRobotData(String data) {
        // DBObject dbObject = (DBObject) JSON.parse(data);
        //robotsCollection.insert(data);
//        ReadData rd = new ReadData(data);
//        ReadData rd = new ObjectMapper().readerFor(ReadData.class).readValue(data);
//        System.out.println(new ObjectMapper().writeValueAsString(rd));
//        robotsCollection.insertOne(rd);
        ReadData rd = new ReadData("Prova", "Prova", "Prova", 1, false, "rfrgirgf");
        try {
            robotsCollection.insertOne(rd);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
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
