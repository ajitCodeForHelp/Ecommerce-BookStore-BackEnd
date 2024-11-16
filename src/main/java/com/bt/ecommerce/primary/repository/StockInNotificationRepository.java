package com.bt.ecommerce.primary.repository;

import com.bt.ecommerce.primary.pojo.StockInNotification;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface StockInNotificationRepository extends MongoRepository<StockInNotification, ObjectId> {
    @Query(value = "{" +
            "  'itemId'   : ?0," +
            "  'deleted'  : ?1" +
            "  'notified' : ?2" +
            "}")
    List<StockInNotification> findByItemIdAndDeletedAndNotified(ObjectId itemId, boolean deleted, boolean notified);

    List<StockInNotification> findByActiveAndDeleted(boolean active, boolean deleted);

    List<StockInNotification> findByDeleted(boolean deleted);

    StockInNotification findByUuid(String uuid);
}
