package Database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoClientSingleton {
    public static final String URL_SERVER = "127.0.0.1";
    public static final int SERVER_PORT = 27017;

    private static MongoClient mongoClient = null;

    public static MongoClient getInstance() {
        if (mongoClient == null) {
            // Create a CodecRegistry containing the PojoCodecProvider instance.
            CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
                    fromProviders(PojoCodecProvider.builder().automatic(true).build()));

            mongoClient = new MongoClient(URL_SERVER,
                    MongoClientOptions.builder().codecRegistry(pojoCodecRegistry).build());
        }
        return mongoClient;
    }
}
