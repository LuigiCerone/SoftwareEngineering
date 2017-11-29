package Database;

import com.mongodb.*;

public class MongoDB {
    public MongoDB(){
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        System.out.println("Mongo is working!!");
    }
}
