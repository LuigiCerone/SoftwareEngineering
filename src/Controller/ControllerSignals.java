package Controller;

import Database.MongoDB;
import Model.ReadData;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class ControllerSignals implements Runnable{
    private MongoDB mongoDB;
    private String readDataToDeserialize;
    private ReadData readData;

    public ControllerSignals(String readDataToDeserialize){
        this.mongoDB = new MongoDB();
        this.readData = readData;
        this.readDataToDeserialize = readDataToDeserialize;
    }

    @Override
    public void run() {
        try {
            readData = new ObjectMapper().readerFor(ReadData.class).readValue(readDataToDeserialize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mongoDB.insertReadingsData(readData);

        mongoDB.insertRobotInCluster(readData);
    }
}
