package main.Model.DAO;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import main.Database.DatabaseConnector;
import main.Model.Cluster;
import main.Model.Zone;
import org.bson.Document;

import java.util.HashMap;

public class ZoneDAO implements ZoneDAO_Interface {

//    @Override
//    public HashMap<String, Zone> populateWithZones(HashMap<String, Cluster> clusters) {
//        Connection connection = databaseConnector.getConnection();
//
//        String query = "SELECT * FROM cluster ORDER BY zoneId; ";
//
//        HashMap<String, Zone> zones = new HashMap<>();
//
//        try {
//            Statement statement = connection.createStatement();
//            ResultSet resultSet = statement.executeQuery(query);
//
//            while (resultSet.next()) {
//                String zoneId = resultSet.getString(Cluster.ZONE_ID);
//                String clusterId = resultSet.getString(Cluster.CLUSTER_ID);
//
//                if (!zones.containsKey(zoneId)) {
//                    // The zone is not present.
//                    zones.put(zoneId, new Zone(zoneId));
//                }
//
//                // The zone already exists, add the cluster to it.
//                zones.get(zoneId).addCluster(clusters.get(clusterId));
//
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return zones;
//    }


    public HashMap<String, Zone> getZones() {
        MongoDatabase mongoDatabase = DatabaseConnector.getInstance().getMongoDatabase();
        MongoCollection<Document> clusters = mongoDatabase.getCollection("clusters");
        HashMap<String, Zone> zones = new HashMap<String, Zone>();

        // We get all the zones from the clusters list using distinct;
        MongoCursor<String> cursor = clusters.distinct("areaId", String.class).iterator();

        while(cursor.hasNext()){
            String zoneId = cursor.next();
            Zone zone = new Zone(zoneId, new ClusterDAO().getClustersForZone(zoneId));
            zones.put(zoneId, zone);
        }
        cursor.close();
        return zones;
    }

    @Override
    public HashMap<String, Zone> populateWithZones(HashMap<String, Cluster> clusters) {
        return null;
    }
}
