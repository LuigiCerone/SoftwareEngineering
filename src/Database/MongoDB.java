package Database;

import Model.ReadData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import java.io.IOException;


public class MongoDB {
    public static final String DATABASE_NAME = "SEProject";
    public static final String DATAREADINGS_COLLECTION = "datareadings";

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private MongoCollection<ReadData> readingsDataCollection;

    public MongoDB() {
        mongoClient = MongoClientSingleton.getInstance();
        mongoDatabase = mongoClient.getDatabase(DATABASE_NAME);
        readingsDataCollection = mongoDatabase.getCollection(DATAREADINGS_COLLECTION, ReadData.class);
    }

    public void getAllRobotsReadingsData() {
        MongoCursor<ReadData> cursor = readingsDataCollection.find().iterator();
        while (cursor.hasNext()) {
            System.out.println(cursor.next());
        }
    }

    public void insertReadingsData(ReadData readData) {
//        ReadData rd = new ReadData("Prova", "Prova", "Prova", 1, false, "rfrgirgf");
        try {
            readingsDataCollection.insertOne(readData);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public void insertRobotInCluster(ReadData readData) {
        String clusterId = readData.getCluster();
        String robotId = readData.getRobot();

        // TODO Map the ReadData into a Cluster with a Robot (that needs to be created if this  the first reading.
        mongoDatabase.getCollection(clusterId, ReadData.class).insertOne(readData);

    }
}
