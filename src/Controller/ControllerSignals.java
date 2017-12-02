package Controller;

import Database.MongoDB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import org.json.JSONObject;

public class ControllerSignals {
    private MongoDB mongoDB;

    public ControllerSignals(){
        this.mongoDB = new MongoDB();
    }

    public void clusterCollectionHandler(String data){
        // Does the collection with the _clusterId attributes exists?
        System.out.println("qui");
        JSONObject json = new JSONObject(data);
        String clusterId = json.getString("_clusterId");
        DBCollection collection = mongoDB.checkForClusterCollectionOrCreateIt(clusterId);
        robotDocumentHandler(collection, json);
    }

    private void robotDocumentHandler(DBCollection collection, JSONObject json){
        // Does a document with the _id robot exists within that collection?
        mongoDB.checkForRobotDocumentOrCreateIt(collection, json);
    }
}
