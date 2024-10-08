//package com.kpis.kpisFood.configuration;
//
//import com.kpis.kpisFood.exception.BadRequestException;
//import com.kpis.kpisFood.exception.InvalidDatabaseAccessException;
//import com.kpis.kpisFood.primary.pojo.common.Database;
//import com.kpis.kpisFood.utils.TextUtils;
//import com.mongodb.ConnectionString;
//import com.mongodb.MongoClientSettings;
//import com.mongodb.client.MongoClient;
//import com.mongodb.client.MongoClients;
//import org.bson.types.ObjectId;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.mongodb.core.MongoTemplate;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//public class MultiMongoDBFactory {
//
//    private MongoClient mongoClient(final String dbUri) {
//        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
//                .applyConnectionString(new ConnectionString(dbUri))
//                .build();
//        return MongoClients.create(mongoClientSettings);
//    }
//
//    private MongoTemplate getDatabase(String dbUri, String pDataBaseName) {
//        MongoClient mongoClient = mongoClient(dbUri);
//        MongoTemplate mongoTemplate = new MongoTemplate(mongoClient, pDataBaseName);
//        return mongoTemplate;
//    }
//
//    Map<ObjectId, MongoTemplate> dataBaseObjects = new HashMap<>();
//
//    // TODO > Future > dataBaseObjects.clear() || dataBaseObjects > Connection Pooling
//
//    public MongoTemplate getClientBrandDbConnection(ObjectId brandId) throws BadRequestException {
//        MongoTemplate mongoTemplate;
//        if (dataBaseObjects.containsKey(brandId)) {
//            mongoTemplate = dataBaseObjects.get(brandId);
//            return mongoTemplate;
//        }
//
//        try {
//            Brand brand = SpringBeanContext.getBean(BrandService.class).findById(brandId);
//            Database database = brand.getDatabaseDetails();
//            if (database == null) {
//                throw new InvalidDatabaseAccessException("Invalid DB credentials");
//            }
//            if (TextUtils.isEmpty(database.getDatabaseUri())) {
//                throw new InvalidDatabaseAccessException("Invalid DB path provided.");
//            }
//            if (TextUtils.isEmpty(database.getDatabaseName())) {
//                throw new InvalidDatabaseAccessException("Invalid DB name provided.");
//            }
//            mongoTemplate = getDatabase(database.getDatabaseUri(), database.getDatabaseName());
//        } catch (Exception e) {
//            throw new BadRequestException(e.getMessage());
//        }
//        dataBaseObjects.put(brandId, mongoTemplate);
//        return mongoTemplate;
//    }
//}