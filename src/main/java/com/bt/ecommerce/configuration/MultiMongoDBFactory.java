package com.bt.ecommerce.configuration;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MultiMongoDBFactory {

    private MongoClient mongoClient(final String dbUri) {
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(dbUri))
                .build();
        return MongoClients.create(mongoClientSettings);
    }
    private MongoTemplate getDatabase(String dbUri, String pDataBaseName) {
        MongoClient mongoClient = mongoClient(dbUri);
        MongoTemplate mongoTemplate = new MongoTemplate(mongoClient, pDataBaseName);
        return mongoTemplate;
    }
}