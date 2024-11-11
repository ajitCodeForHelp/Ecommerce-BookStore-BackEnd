package com.bt.ecommerce.primary.repository;

import com.bt.ecommerce.primary.pojo.DynamicField;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DynamicFieldRepository extends MongoRepository<DynamicField, ObjectId> {
    DynamicField findByUuid(String uuid);

    List<DynamicField> findByActiveAndDeleted(boolean active, boolean deleted);

    List<DynamicField> findByDeleted(boolean deleted);
}
