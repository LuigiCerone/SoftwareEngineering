package Controller;

import Database.Database;
import Database.MongoDB;
import Model.ReadData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.io.IOException;

public class ControllerSignals implements Runnable{
//    private MongoDB mongoDB;

    private Database db;
    private String readDataToDeserialize;
    private ReadData readData;

    public ControllerSignals(String readDataToDeserialize){
//        this.mongoDB = new MongoDB();
        this.db = new Database();
        this.readData = new ReadData(readDataToDeserialize);
        System.out.println("Ho fatto");
    }

    @Override
    public void run() {
//        try {
//            // readData = new ObjectMapper().readerFor(ReadData.class).readValue(readDataToDeserialize);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        db.insertReadData(this.readData);

//        mongoDB.insertReadingsData(readData);


//        mongoDB.insertRobotInCluster(readData);
    }
}
