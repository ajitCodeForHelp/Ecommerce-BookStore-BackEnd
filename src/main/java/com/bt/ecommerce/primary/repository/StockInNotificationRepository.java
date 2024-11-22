package com.bt.ecommerce.primary.repository;

import com.bt.ecommerce.primary.pojo.StockInNotification;
import com.bt.ecommerce.primary.pojo.user.SystemUser;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface StockInNotificationRepository extends MongoRepository<StockInNotification, ObjectId> {
    List<StockInNotification> findByItemIdAndDeleted(ObjectId itemId, boolean deleted);

    @Query(value = "" +
            "{" +
            "  'active'  : { '$eq' : ?0 }," +
            "  'deleted' : { '$eq' : ?1 }," +
            "}")
    List<StockInNotification> findByActiveAndDeleted(boolean active, boolean deleted);


    @Query(value = "" +
            "{" +
            "  'active'  : { '$eq' : ?0 }," +
            "  'notifiedToCustomer' : { '$eq' : ?1 }," +
            "}")
    List<StockInNotification> findByActiveAndNotified(boolean active, boolean notifiedToCustomer);
    @Query(value = "" +
            "{" +
            "  'deleted' : { '$eq' : ?0 }," +
            "}")
    List<StockInNotification> findByDeleted(boolean deleted);

    StockInNotification findByUuid(String uuid);

}
