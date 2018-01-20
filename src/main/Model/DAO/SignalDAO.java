package main.Model.DAO;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import main.Database.DatabaseConnector;
import main.Model.ReadData;
import main.Model.Robot;
import main.Model.Signal;
import org.bson.Document;

public class SignalDAO implements SignalDAO_Interface {
    @Override
    public void update(ReadData readData) {
        MongoDatabase mongoDatabase = DatabaseConnector.getInstance().getMongoDatabase();
        MongoCollection<Document> robots = mongoDatabase.getCollection("robots");

        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.append(Robot.ROBOT_ID, readData.getRobot());
        searchQuery.append(Robot.SIGNALS + "." + Signal.SIGNAL_NUMER, readData.getSignal());

        // Update clause of the query.
        BasicDBObject updateFields = new BasicDBObject();
        updateFields.append(Robot.SIGNALS + ".$." + Signal.SIGNAL_VALUE, readData.getValue());
        updateFields.append(Robot.SIGNALS + ".$." + Signal.SIGNAL_TIMESTAMP, readData.getTimestamp().getTime());
        BasicDBObject setQuery = new BasicDBObject();
        setQuery.append("$set", updateFields);

        robots.updateOne(searchQuery, setQuery);
    }
}
