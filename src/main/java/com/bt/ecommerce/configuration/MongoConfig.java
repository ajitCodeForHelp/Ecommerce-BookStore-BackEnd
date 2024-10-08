package com.bt.ecommerce.configuration;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.uri}")
    public String DataBaseURI;
    @Value("${spring.data.mongodb.database}")
    public String DataBase;

    @Override
    protected String getDatabaseName() {
        return DataBase;
    }

    @Override
    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create(DataBaseURI);
    }

    public String getClientDataBase(String preFix, Long clientSequenceId, Long brandSequenceId, String brandTitle) {
        return preFix + "_" + clientSequenceId + "_" + brandSequenceId + "_" + brandTitle;
    }
}