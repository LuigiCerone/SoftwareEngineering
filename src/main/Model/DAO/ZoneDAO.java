package main.Model.DAO;

import main.Database.Database;
import main.Model.Cluster;
import main.Model.Zone;

import java.sql.*;
import java.util.HashMap;

public class ZoneDAO implements ZoneDAO_Interface {
    
    @Override
    public HashMap<String, Zone> populateWithZones(HashMap<String, Cluster> clusters) {
        Connection connection = Database.getConnection();

        String query = "SELECT * FROM cluster ORDER BY zoneId; ";

        HashMap<String, Zone> zones = new HashMap<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String zoneId = resultSet.getString(Cluster.ZONE_ID);
                String clusterId = resultSet.getString(Cluster.CLUSTER_ID);

                if (!zones.containsKey(zoneId)) {
                    // The zone is not present.
                    zones.put(zoneId, new Zone(zoneId));
                }

                // The zone already exists, add the cluster to it.
                zones.get(zoneId).addCluster(clusters.get(clusterId));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return zones;
    }
}
