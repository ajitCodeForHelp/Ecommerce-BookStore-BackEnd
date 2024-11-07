package com.bt.ecommerce.primary.repository;

import com.bt.ecommerce.primary.pojo.Address;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface AddressRepository extends MongoRepository<Address, ObjectId> {
    Address findByUuid(String uuid);

    @Query(value = "" +
            "{" +
            "  'customerId'  : { '$eq' : ?0 }," +
            "  'active'  : { '$eq' : ?1 }," +
            "  'deleted' : { '$eq' : ?2 }," +
            "}")
    List<Address> findByCustomerIdAndActiveAndDeleted(ObjectId customerId, boolean active, boolean deleted);

    List<Address> findByCustomerIdAndDeleted(ObjectId customerId, boolean deleted);
    List<Address> findByCustomerId(ObjectId customerId);
}
