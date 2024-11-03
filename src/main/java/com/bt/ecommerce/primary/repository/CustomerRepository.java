package com.bt.ecommerce.primary.repository;

import com.bt.ecommerce.primary.pojo.user.Customer;
import com.bt.ecommerce.primary.pojo.user.SystemUser;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, ObjectId> {

    Customer findFirstByUsername(String username);

    boolean existsByIsdCodeAndMobile(String isdCode, String mobile);

    boolean existsByEmail(String email);

    Customer findByUuid(String uuid);

    Customer findFirstByIsdCodeAndMobileAndId(String isdCode, String mobile, ObjectId id);

    Customer findFirstByEmailAndId(String email, ObjectId id);

    Page<Customer> findByDeleted(Boolean deleted, String search, Pageable pageable);
}
