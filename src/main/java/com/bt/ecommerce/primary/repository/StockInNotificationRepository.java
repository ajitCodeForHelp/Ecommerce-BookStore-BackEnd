package com.bt.ecommerce.primary.repository;

import com.bt.ecommerce.primary.pojo.StockInNotification;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface StockInNotificationRepository extends MongoRepository<StockInNotification, ObjectId> {
    List<StockInNotification> findByItemIdAndDeleted(ObjectId itemId, boolean deleted);
}
