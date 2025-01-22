package com.bt.ecommerce.primary.repository;

import com.bt.ecommerce.primary.pojo.CourierPartner;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CourierPartnerRepository extends MongoRepository<CourierPartner, ObjectId> {
    CourierPartner findByUuid(String uuid);

    List<CourierPartner> findByActiveAndDeleted(boolean active, boolean deleted);

    List<CourierPartner> findByDeleted(boolean deleted);
}
