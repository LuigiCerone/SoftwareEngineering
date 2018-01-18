package main.Database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

public class DatabaseConnector {
    private static MongoClient mongoClient;
    // Singleton for MongoClient
    // Creates a single connection pool internally

    private MongoDatabase database = null;

    private MongoClient getMongoClient() {
        if (mongoClient == null) {
            mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
            database = mongoClient.getDatabase("SEProject");
        }
        return mongoClient;
    }

    public MongoDatabase getDatabase() {
        if (database == null || mongoClient == null)
            getMongoClient();
        return database;
    }
}
