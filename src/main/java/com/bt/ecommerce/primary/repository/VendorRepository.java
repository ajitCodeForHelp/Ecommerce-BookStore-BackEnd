package com.bt.ecommerce.primary.repository;

import com.bt.ecommerce.primary.pojo.Publisher;
import com.bt.ecommerce.primary.pojo.Vendor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface VendorRepository extends MongoRepository<Vendor, ObjectId> {
    Vendor findByUuid(String uuid);

    List<Vendor> findByActiveAndDeleted(boolean active, boolean deleted);

    List<Vendor> findByDeleted(boolean deleted);
}
