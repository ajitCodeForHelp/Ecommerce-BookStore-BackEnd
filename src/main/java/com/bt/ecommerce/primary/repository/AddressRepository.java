package com.bt.ecommerce.primary.repository;

import com.bt.ecommerce.primary.pojo.Address;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AddressRepository extends MongoRepository<Address, ObjectId> {
    Address findByUuid(String uuid);

    List<Address> findByCustomerUuidAndActive(String uuid, boolean b);
}
