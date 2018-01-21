package main.Model.DAO;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.BulkWriteOptions;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.UpdateOneModel;
import main.Controller.ControllerSignals;
import main.Database.DatabaseConnector;
import main.Model.History;
import main.Model.ReadData;
import main.Model.Robot;
import org.bson.Document;

import java.sql.Timestamp;
import java.util.*;

public class RobotDAO implements RobotDAO_Interface {

    @Override
    public HashSet<Robot> getRobotsForCluster(String clusterId) {
        MongoDatabase mongoDatabase = DatabaseConnector.getInstance().getMongoDatabase();
        MongoCollection<Document> robotsCollection = mongoDatabase.getCollection("robots");
        HashSet<Robot> robots = new HashSet<Robot>();

        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put(Robot.CLUSTER_ID, clusterId);

        MongoCursor<Document> cursor = robotsCollection.find(searchQuery).iterator();
        try {
            while (cursor.hasNext()) {
                Document item = cursor.next();

                Robot robot = new Robot();
                robot.setRobotId(item.getString(Robot.ROBOT_ID));
                robot.setClusterId(item.getString(Robot.CLUSTER_ID));
                robot.setStartUpTime(new Timestamp(item.getLong(Robot.START_UP_TIME)));
                robot.setInefficiencyRate(item.getDouble(Robot.INEFFICIENCY_RATE));
                robot.setCountInefficiencyComponents(item.getInteger(Robot.COUNT_INEFFICIENCY_COMPONENTS));

                // Robot histories.
                robot.setHistories((ArrayList<Document>) item.get(Robot.HISTORIES));
                robots.add(robot);
            }
        } finally {
            cursor.close();
        }
        return robots;
    }

    @Override
    public HashSet<Robot> getRobotsInfoNoHistory(String clusterId) {

        MongoDatabase mongoDatabase = DatabaseConnector.getInstance().getMongoDatabase();
        MongoCollection<Document> robotsCollection = mongoDatabase.getCollection("robots");
        HashSet<Robot> robots = new HashSet<Robot>();

        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put(Robot.CLUSTER_ID, clusterId);

        MongoCursor<Document> cursor = robotsCollection.find(searchQuery).iterator();
        try {
            while (cursor.hasNext()) {
                Document item = cursor.next();

                Robot robot = new Robot();
                robot.setRobotId(item.getString(Robot.ROBOT_ID));
                robot.setClusterId(item.getString(Robot.CLUSTER_ID));
                robot.setInefficiencyRate(item.getDouble(Robot.INEFFICIENCY_RATE));
                robots.add(robot);
            }
        } finally {
            cursor.close();
        }
        return robots;
    }

    @Override
    public void insert(ReadData readData) {
        MongoDatabase mongoDatabase = DatabaseConnector.getInstance().getMongoDatabase();
        MongoCollection<Document> robots = mongoDatabase.getCollection("robots");

        Timestamp now = new Timestamp(System.currentTimeMillis());
        Timestamp startUp = (now.after(readData.getTimestamp()) ? readData.getTimestamp() : now);

        Document newRobot = new Document()
                .append(Robot.ROBOT_ID, readData.getRobot())
                .append(Robot.CLUSTER_ID, readData.getCluster())
                .append(Robot.INEFFICIENCY_RATE, 0.0)
                .append(Robot.COUNT_INEFFICIENCY_COMPONENTS, 0)
                .append(Robot.START_UP_TIME, startUp.getTime())
                .append(Robot.START_DOWN_TIME, null)
                .append(Robot.HISTORY, new History(readData, startUp, 0).toDocument());
        robots.insertOne(newRobot);
    }

    @Override
    public void updateCountAndStartDown(Robot robot, ReadData readData) {
        MongoDatabase mongoDatabase = DatabaseConnector.getInstance().getMongoDatabase();
        MongoCollection<Document> robots = mongoDatabase.getCollection("robots");


        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("_id", robot.getRobotId());

        BasicDBObject updateFields = new BasicDBObject();
        updateFields.append(Robot.COUNT_INEFFICIENCY_COMPONENTS, robot.getCountInefficiencyComponents());
        updateFields.append(Robot.START_DOWN_TIME, readData.getTimestamp().getTime());
        BasicDBObject setQuery = new BasicDBObject();
        setQuery.append("$set", updateFields);

        robots.updateOne(searchQuery, setQuery);
    }

    @Override
    public void updateCountAndStopDown(Robot robot, ReadData readData) {
        MongoDatabase mongoDatabase = DatabaseConnector.getInstance().getMongoDatabase();
        MongoCollection<Document> robots = mongoDatabase.getCollection("robots");

        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put(Robot.ROBOT_ID, robot.getRobotId());

        BasicDBObject updateFields = new BasicDBObject();
        updateFields.append(Robot.COUNT_INEFFICIENCY_COMPONENTS, robot.getCountInefficiencyComponents());
        updateFields.append(Robot.START_DOWN_TIME, null);
        BasicDBObject setQuery = new BasicDBObject();
        setQuery.append("$set", updateFields);

        robots.updateOne(searchQuery, setQuery);
    }

    @Override
    public void updateCount(Robot robot) {
        MongoDatabase mongoDatabase = DatabaseConnector.getInstance().getMongoDatabase();
        MongoCollection<Document> robots = mongoDatabase.getCollection("robots");

        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("_id", robot.getRobotId());

        BasicDBObject updateFields = new BasicDBObject();
        updateFields.append(Robot.COUNT_INEFFICIENCY_COMPONENTS, robot.getCountInefficiencyComponents());
        BasicDBObject setQuery = new BasicDBObject();
        setQuery.append("$set", updateFields);

        robots.updateOne(searchQuery, setQuery);
    }

    @Override
    public void updateIR(HashMap<String, Double> clustersIR) {
        MongoDatabase mongoDatabase = DatabaseConnector.getInstance().getMongoDatabase();
        MongoCollection<Document> robotsCollection = mongoDatabase.getCollection("robots");

        LinkedList<UpdateOneModel<? extends Document>> bulkUpdatesList = new LinkedList<>();
        for (Map.Entry<String, Double> entry : clustersIR.entrySet()) {
            bulkUpdatesList.add(new UpdateOneModel<>(new Document(Robot.ROBOT_ID, entry.getKey()),
                    new Document("$set", new Document(Robot.INEFFICIENCY_RATE, entry.getValue()))));
        }
        robotsCollection.bulkWrite(bulkUpdatesList, new BulkWriteOptions().ordered(false));
    }


    @Override
    public Robot getRobot(ReadData readData) {
        MongoDatabase mongoDatabase = DatabaseConnector.getInstance().getMongoDatabase();
        MongoCollection<Document> robotsCollection = mongoDatabase.getCollection("robots");

        Robot robot = null;

        // Where clause of the query.
        Document whereQuery = new Document("_id", readData.getRobot());

        // Item to insert if no cluster is already present.
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Timestamp startUp = (now.after(readData.getTimestamp()) ? readData.getTimestamp() : now);

        HashSet<Document> historiesAsDoc = new HashSet<>();
        historiesAsDoc.add(new History(readData, startUp, 0).toDocument());

        ArrayList<Document> signalsAsDoc = new ControllerSignals().createSignals(readData.getTimestamp().getTime());

//        Document histories = new Document(new Document(Robot.HISTORY, ));
        Document setOnInsert = new Document()
                .append(Robot.ROBOT_ID, readData.getRobot())
                .append(Robot.CLUSTER_ID, readData.getCluster())
                .append(Robot.INEFFICIENCY_RATE, 0.0)
                .append(Robot.COUNT_INEFFICIENCY_COMPONENTS, 0)
                .append(Robot.START_UP_TIME, startUp.getTime())
                .append(Robot.START_DOWN_TIME, null)
                .append(Robot.SIGNALS, signalsAsDoc)
                .append(Robot.HISTORIES, historiesAsDoc);
        Document update = new Document("$setOnInsert", setOnInsert);

        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions();
        options.returnDocument(ReturnDocument.AFTER);
        options.upsert(true);

        Document document = robotsCollection.findOneAndUpdate(whereQuery, update, options);
        robot = new Robot(document);
        return robot;
    }
}
