package main.Model.DAO;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.UpdateOneModel;
import main.Database.DatabaseConnector;
import main.Model.Cluster;
import main.Model.History;
import main.Model.ReadData;
import main.Model.Robot;
import org.bson.Document;

import java.sql.Timestamp;
import java.util.*;

public class ClusterDAO implements ClusterDAO_Interface {
    public Cluster getCluster(ReadData readData) {
        MongoDatabase mongoDatabase = DatabaseConnector.getInstance().getMongoDatabase();
        MongoCollection<Document> clustersCollection = mongoDatabase.getCollection("clusters");

        Cluster cluster = null;

        // Where clause of the query.
        Document whereQuery = new Document("_id", readData.getCluster());

        // Item to insert if no cluster is already present.
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Timestamp startUp = (now.after(readData.getTimestamp()) ? readData.getTimestamp() : now);

        HashSet<Document> historiesAsDoc = new HashSet<>();
        historiesAsDoc.add(new History(readData, startUp, 1).toDocument());

        Document setOnInsert = new Document()
                .append(Cluster.CLUSTER_ID, readData.getCluster())
                .append(Cluster.ZONE_ID, readData.getZone())
                .append(Cluster.INEFFICIENCY_RATE, 0.0)
                .append(Cluster.COUNT_INEFFICIENCY_COMPONENTS, 0)
                .append(Cluster.START_UP_TIME, startUp.getTime())
                .append(Cluster.START_DOWN_TIME, null)
                .append(Cluster.HISTORIES, historiesAsDoc);
        Document update = new Document("$setOnInsert", setOnInsert);

        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions();
        options.returnDocument(ReturnDocument.AFTER);
        options.upsert(true);

        Document document = clustersCollection.findOneAndUpdate(whereQuery, update, options);
        cluster = new Cluster(document);

        return cluster;
    }

    public HashSet<Cluster> getClustersForZone(String zoneId) {
        MongoDatabase mongoDatabase = DatabaseConnector.getInstance().getMongoDatabase();
        MongoCollection<Document> clustersCollection = mongoDatabase.getCollection("clusters");

        HashSet<Cluster> clusters = new HashSet<Cluster>();

        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put(Cluster.ZONE_ID, zoneId);

        MongoCursor<Document> cursor = clustersCollection.find(searchQuery).iterator();
        try {
            while (cursor.hasNext()) {
                Document item = cursor.next();

                Cluster cluster = new Cluster();
                cluster.setClusterId(item.getString(Cluster.CLUSTER_ID));
                cluster.setZoneId(item.getString(Cluster.ZONE_ID));
                cluster.setStartUpTime(new Timestamp(item.getLong(Cluster.START_UP_TIME)));
                cluster.setInefficiencyRate(item.getDouble(Cluster.INEFFICIENCY_RATE));
                cluster.setCountInefficiencyComponents(item.getInteger(Cluster.COUNT_INEFFICIENCY_COMPONENTS));

                // We need to get the robots in this cluster.
                HashSet<Robot> robots = new RobotDAO().getRobotsForCluster(cluster.getClusterId());
                cluster.setRobotsList(robots);
                clusters.add(cluster);
            }
        } finally {
            cursor.close();
        }
        return clusters;
    }

    @Override
    public void insert(ReadData readData) {
        MongoDatabase mongoDatabase = DatabaseConnector.getInstance().getMongoDatabase();
        MongoCollection<Document> clusters = mongoDatabase.getCollection("clusters");

        Timestamp now = new Timestamp(System.currentTimeMillis());
        Document newCluster = new Document()
                .append(Cluster.CLUSTER_ID, readData.getCluster())
                .append(Cluster.ZONE_ID, readData.getZone())
                .append(Cluster.INEFFICIENCY_RATE, 0.0)
                .append(Cluster.COUNT_INEFFICIENCY_COMPONENTS, 0)
                .append(Cluster.START_UP_TIME, (now.after(readData.getTimestamp()) ? readData.getTimestamp().toString() : now.toString()))
                .append(Cluster.START_DOWN_TIME, null);
        clusters.insertOne(newCluster);
    }


    @Override
    public Cluster findClusterByIdOrInsert(ReadData readData) {
        return null;
    }

    @Override
    public void updateCountAndStartDown(Cluster cluster, ReadData readData) {
        MongoDatabase mongoDatabase = DatabaseConnector.getInstance().getMongoDatabase();
        MongoCollection<Document> clusters = mongoDatabase.getCollection("clusters");

        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put(Cluster.CLUSTER_ID, cluster.getClusterId());

        BasicDBObject updateFields = new BasicDBObject();
        updateFields.append(Cluster.COUNT_INEFFICIENCY_COMPONENTS, cluster.getCountInefficiencyComponents());
        updateFields.append(Cluster.START_DOWN_TIME, readData.getTimestamp().getTime());
        BasicDBObject setQuery = new BasicDBObject();
        setQuery.append("$set", updateFields);

        clusters.updateOne(searchQuery, setQuery);
    }

    @Override
    public void updateCountAndStopDown(Cluster cluster, ReadData readData, long downTimeDiffCluster) {
        MongoDatabase database = DatabaseConnector.getInstance().getMongoDatabase();
        MongoCollection<Document> clusters = database.getCollection("clusters");

        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put(Cluster.CLUSTER_ID, cluster.getClusterId());

        BasicDBObject updateFields = new BasicDBObject();
        updateFields.append(Cluster.COUNT_INEFFICIENCY_COMPONENTS, cluster.getCountInefficiencyComponents());
        updateFields.append(Cluster.START_DOWN_TIME, null);
        BasicDBObject setQuery = new BasicDBObject();
        setQuery.append("$set", updateFields);

        clusters.updateOne(searchQuery, setQuery);
    }

    @Override
    public void processClusterIR() {

    }

    @Override
    public void updateIR(HashMap<String, Float> clustersIR) {
        MongoDatabase mongoDatabase = DatabaseConnector.getInstance().getMongoDatabase();
        MongoCollection<Document> clustersCollection = mongoDatabase.getCollection("clusters");

        LinkedList<UpdateOneModel<? extends Document>> bulkUpdatesList = new LinkedList<>();
        for (Map.Entry<String, Float> entry : clustersIR.entrySet()) {
            bulkUpdatesList.add(new UpdateOneModel<>(new Document(Cluster.CLUSTER_ID, entry.getKey()),
                    new Document("$set", new Document(Cluster.INEFFICIENCY_RATE, entry.getValue()))));
        }
        clustersCollection.bulkWrite(bulkUpdatesList);
    }

    @Override
    public HashSet<Cluster> getAllClusters() {
        MongoDatabase mongoDatabase = DatabaseConnector.getInstance().getMongoDatabase();
        MongoCollection<Document> clustersCollection = mongoDatabase.getCollection("clusters");

        HashSet<Cluster> clusters = new HashSet<Cluster>();
        MongoCursor<Document> cursor = clustersCollection.find().iterator();
        try {
            while (cursor.hasNext()) {
                Document item = cursor.next();

                Cluster cluster = new Cluster();
                cluster.setClusterId(item.getString(Cluster.CLUSTER_ID));
                cluster.setZoneId(item.getString(Cluster.ZONE_ID));
                cluster.setStartUpTime(new Timestamp(item.getLong(Cluster.START_UP_TIME)));
                cluster.setInefficiencyRate(item.getDouble(Cluster.INEFFICIENCY_RATE));
                cluster.setCountInefficiencyComponents(item.getInteger(Cluster.COUNT_INEFFICIENCY_COMPONENTS));
                // Cluster histories.
                cluster.setHistories((ArrayList<Document>) item.get(Cluster.HISTORIES));

                // We need to get the robots in this cluster.
                HashSet<Robot> robots = new RobotDAO().getRobotsForCluster(cluster.getClusterId());
                cluster.setRobotsList(robots);
                clusters.add(cluster);
            }
        } finally {
            cursor.close();
        }
        return clusters;
    }
}
