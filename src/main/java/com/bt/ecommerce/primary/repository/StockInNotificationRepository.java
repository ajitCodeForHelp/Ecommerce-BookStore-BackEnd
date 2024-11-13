package com.bt.ecommerce.primary.repository;

import com.bt.ecommerce.primary.pojo.StockInNotification;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface StockInNotificationRepository extends MongoRepository<StockInNotification, ObjectId> {
//    StockInNotification findByUuid(String uuid);
//
//    List<StockInNotification> findByActiveAndDeleted(boolean active, boolean deleted);
//
//    List<StockInNotification> findByDeleted(boolean deleted);

    List<StockInNotification> findByItemId(ObjectId itemId);
}
