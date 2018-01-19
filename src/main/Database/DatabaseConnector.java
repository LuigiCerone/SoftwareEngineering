package main.Database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

public class DatabaseConnector {
    // Singleton for MongoClient
    // Creates a single connection pool internally

    private MongoClient mongoClient = null;
    private MongoDatabase mongoDatabase = null;
    private static DatabaseConnector instance = null;

    private DatabaseConnector() {
        try {
            mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
            mongoDatabase = mongoClient.getDatabase("SEProject");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public static MongoDatabase getDatabase() {
////        if (mongoDatabase == null || mongoClient == null)
//        return mongoDatabase;
//    }

    public static DatabaseConnector getInstance() {
        if(instance == null) {
            instance = new DatabaseConnector();
        }
        return instance;
    }

    public MongoDatabase getMongoDatabase() {
        return mongoDatabase;
    }
}
