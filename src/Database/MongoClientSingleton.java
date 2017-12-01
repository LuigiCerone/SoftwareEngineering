package Database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class MongoClientSingleton {
    public static final String URL_SERVER = "127.0.0.1";
    public static final int URL_SERVER_PORT = 27017;

    private static MongoClient mongoClient = null;

    public static MongoClient getInstance() {
        if (mongoClient == null) {
            mongoClient = new MongoClient(new MongoClientURI("mongodb:// " + URL_SERVER
                    + ":" + URL_SERVER_PORT));
        }
        return mongoClient;
    }
}
