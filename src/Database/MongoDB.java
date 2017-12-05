package Database;

import Model.ReadData;
import Model.Robot;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import org.bson.BSONObject;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;

import java.io.IOException;
import java.util.Map;
import java.util.Set;


public class MongoDB {
    public static final String DATABASE_NAME = "SEProject";
    public static final String DATAREADINGS_COLLECTION = "datareadings";

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private MongoCollection<ReadData> readingsDataCollection;
    private ObjectMapper objectMapper;

    public MongoDB() {
        objectMapper = new ObjectMapper();
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

        // TODO verificare che la collection esiste
        // TODO verificare che il robot è presente nella collection
        // TODO inserirlo con dati di default oppure aggiornarlo se presente

        // TODO Map the ReadData into a Cluster with a Robot (that needs to be created if this  the first reading).
        //mongoDatabase.getCollection(clusterId, Robot.class).insertOne(readData);

        // Gson gson = new Gson();

        MongoCollection<Robot> robotInOneCluster = mongoDatabase.getCollection(clusterId, Robot.class);
        Document filter = new Document();
        filter.put("robotId", robotId);

        Robot robot = new Robot(robotId, clusterId, readData.getZone());
        try {
            String stringRobot = this.objectMapper.writerFor(Robot.class).writeValueAsString(robot);
//            Robot existing = robotInOneCluster.findOneAndUpdate(filter, Document.parse(stringRobot), new FindOneAndUpdateOptions().upsert(true));
            robotInOneCluster.insertOne(robot);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
