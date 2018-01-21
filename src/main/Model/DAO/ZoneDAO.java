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

    @Override
    public HashMap<String, Zone> getZones() {
        MongoDatabase mongoDatabase = DatabaseConnector.getInstance().getMongoDatabase();
        MongoCollection<Document> clusters = mongoDatabase.getCollection("clusters");

        HashMap<String, Zone> zones = new HashMap<String, Zone>();

        // We get all the zones from the clusters list using distinct;
        MongoCursor<String> cursor = clusters.distinct(Cluster.ZONE_ID, String.class).iterator();

        try {
            while (cursor.hasNext()) {
                String zoneId = cursor.next();
//                Zone zone = new Zone(zoneId, new ZoneDAO().getZoneWithCluster(zoneId));
                Zone zone = new Zone(zoneId, new ClusterDAO().getClustersForZone(zoneId));
                zones.put(zoneId, zone);
            }
        } finally {
            cursor.close();
        }
        return zones;
    }
}
