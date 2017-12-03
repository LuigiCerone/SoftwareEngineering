package Database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;
import eu.dozd.mongo.MongoMapper;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;

public class MongoClientSingleton {
    public static final String URL_SERVER = "127.0.0.1";
    public static final int SERVER_PORT = 27017;

    private static MongoClient mongoClient = null;

    public static MongoClient getInstance() {
        if (mongoClient == null) {
            CodecRegistry codecRegistry = CodecRegistries.fromProviders(MongoMapper.getProviders());
            MongoClientOptions settings = MongoClientOptions.builder().codecRegistry(codecRegistry).build();
            mongoClient = new MongoClient(new ServerAddress(URL_SERVER, SERVER_PORT), settings);
        }
        return mongoClient;
    }
}
