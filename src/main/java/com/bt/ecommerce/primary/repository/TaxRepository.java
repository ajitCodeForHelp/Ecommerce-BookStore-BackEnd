package com.bt.ecommerce.primary.repository;

import com.bt.ecommerce.primary.pojo.Tax;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TaxRepository extends MongoRepository<Tax, ObjectId> {
    Tax findByUuid(String uuid);

    List<Tax> findByActiveAndDeleted(boolean active, boolean deleted);

    List<Tax> findByDeleted(boolean deleted);
}
