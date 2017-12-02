package Database;

import com.mongodb.*;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;

public class MongoDB {
    public static final String DATABASE_NAME = "robots";
    public static final String ROBOTS_COLLECTION = "robots";

    private DB mongoDatabase = this.getDatabase();
    private DBCollection robotsCollection = this.setDefaultCollection();

    public MongoDB() {
    }

    public DB getDatabase() {
        return MongoClientSingleton.getInstance().getDB(DATABASE_NAME);
    }

    public DBCollection setDefaultCollection() {
        return mongoDatabase.getCollection(ROBOTS_COLLECTION);
    }

    public void getAllRobotsData() {
        DBCursor cursor = robotsCollection.find();

        while (cursor.hasNext()) {
            System.out.println(cursor.next());
        }
    }

    public void insertRobotData(String data) {
        DBObject dbObject = (DBObject) JSON.parse(data);
        robotsCollection.insert(dbObject);
    }

    // Assumption, in our database empty collections don't exists.
    public DBCollection checkForClusterCollectionOrCreateIt(String clusterId) {
//        DBCollection table = mongoDatabase.getCollection(clusterId);
//        return table;
//        if(table.count() > 0){
//            return table;
//        }
//        else{
//            DBCollection collection = mongoDatabase.createCollection(clusterId, null);
//            return collection;
//        }
        boolean collectionExists = mongoDatabase.collectionExists(clusterId);
        DBCollection collection = null;
        if (!collectionExists) {
            collection = mongoDatabase.createCollection(clusterId, new BasicDBObject("capped", false));
        }
        return collection;
    }

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

    }

    public void insertRobotInCluster(JsonObject json) {
        JsonObject jsonObject = json.getAsJsonObject("robot");
        String clusterId = jsonObject.get("_cluster").getAsString();
        String robotId = jsonObject.get("_id").getAsString();

        mongoDatabase.getCollection(clusterId).insert(json);

    }
}
