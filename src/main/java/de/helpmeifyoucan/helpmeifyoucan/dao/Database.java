package de.helpmeifyoucan.helpmeifyoucan.dao;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import de.helpmeifyoucan.helpmeifyoucan.config.DatabaseConfig;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Autowired;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class Database {

    private MongoClient client;
    public static CodecRegistry pojoCodec;
    public MongoDatabase database;

    @Autowired
    public Database(DatabaseConfig config) {
        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        pojoCodec = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);

        var stringBuilder = new StringBuilder();
        stringBuilder.append(config.getProtocol()).append("://");

        if (config.getUser() != null && config.getPassword() != null) {
            stringBuilder.append(config.getUser()).append(":").append(config.getPassword()).append("@");
        }
        stringBuilder.append(config.getHost());
        var uri = new ConnectionString(stringBuilder.toString());
        var settings = MongoClientSettings.builder().applyConnectionString(uri).codecRegistry(pojoCodec).build();
        this.client = MongoClients.create(settings);

        this.database = this.client.getDatabase(config.getName());
    }

    public MongoDatabase getDatabase() {
        return this.database;
    }

}