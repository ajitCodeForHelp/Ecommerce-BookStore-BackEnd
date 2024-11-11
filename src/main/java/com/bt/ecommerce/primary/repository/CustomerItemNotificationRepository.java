package com.bt.ecommerce.primary.repository;

import com.bt.ecommerce.primary.pojo.CustomerItemNotification;
import com.bt.ecommerce.primary.pojo.DynamicField;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CustomerItemNotificationRepository extends MongoRepository<CustomerItemNotification, ObjectId> {
    CustomerItemNotification findByUuid(String uuid);

    List<CustomerItemNotification> findByActiveAndDeleted(boolean active, boolean deleted);

    List<CustomerItemNotification> findByDeleted(boolean deleted);
}
