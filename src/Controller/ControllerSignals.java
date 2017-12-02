package Controller;

import Database.MongoDB;

public class ControllerSignals implements Runnable{
    private MongoDB mongoDB;
    private String data;

    public ControllerSignals(String data){
        this.mongoDB = new MongoDB();
        this.data = data;
    }

    private void clusterCollectionHandler(){
        // Does the collection with the _clusterId attributes exists?
//        Gson gson = new Gson();
//        System.out.println(json.toString());
//        String clusterId = json.getJSONObject("robot").getString("_cluster");
//        DBCollection collection = mongoDB.checkForClusterCollectionOrCreateIt(clusterId);
//        robotDocumentHandler(collection, json);
    }

    private void insertRobotDocument(){
        // Does a document with the _id robot exists within that collection?
//        JsonParser parser = new JsonParser();
//        JsonObject json = parser.parse(data).getAsJsonObject();
//        mongoDB.insertRobotInCluster(json);
    }

    @Override
    public void run() {
       // clusterCollectionHandler();
        insertRobotDocument();

    }
}
