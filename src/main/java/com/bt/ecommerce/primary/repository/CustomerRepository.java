package com.bt.ecommerce.primary.repository;

import com.bt.ecommerce.primary.pojo.user.Customer;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomerRepository extends MongoRepository<Customer, ObjectId> {

    Customer findFirstByUsername(String username);

    boolean existsByIsdCodeAndMobile(String isdCode, String mobile);

    boolean existsByEmail(String email);

    Customer findByUuid(String uuid);

    Customer findFirstByIsdCodeAndMobileAndId(String isdCode, String mobile, ObjectId id);

    Customer findFirstByEmailAndId(String email, ObjectId id);

    Page<Customer> findByDeleted(Boolean deleted, String search, Pageable pageable);

    List<Customer> findByActiveAndDeleted(boolean active, boolean deleted);

    List<Customer> findByDeleted(boolean deleted);

    Customer findFirstByIsdCodeAndMobile(String isdCode, String mobile);

    @Query(value = "{ 'createdAt': { '$gte': ?0, '$lte': ?1 } }", count = true)
    Integer countCustomersByCreatedAt(LocalDateTime start, LocalDateTime end);

}
