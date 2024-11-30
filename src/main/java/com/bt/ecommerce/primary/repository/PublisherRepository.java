package com.bt.ecommerce.primary.repository;

import com.bt.ecommerce.primary.pojo.Publisher;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PublisherRepository extends MongoRepository<Publisher, ObjectId> {
    Publisher findByUuid(String uuid);

    List<Publisher> findByActiveAndDeleted(boolean active, boolean deleted);

    List<Publisher> findByDeleted(boolean deleted);
}
