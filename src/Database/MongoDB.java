package Database;

import com.mongodb.*;
import com.mongodb.client.MongoDatabase;

public class MongoDB {
    public static final String DATABASE_NAME = "robots";
    public static final String ROBOTS_COLLECTION = "robots";

    private DB mongoDatabase = null;
    private DBCollection robotsCollection = null;

    public MongoDB() {
        this.getDatabase();
        this.getCollection();
    }



    public void getDatabase(){
        mongoDatabase = MongoClientSingleton.getInstance().getDB(DATABASE_NAME);
    }

    public void getCollection(){
        robotsCollection = mongoDatabase.getCollection(ROBOTS_COLLECTION);
    }

    public void getAllRobotsData(){
        DBCursor cursor = robotsCollection.find();

        while(cursor.hasNext()){
            System.out.println(cursor.next());
        }
    }
}
