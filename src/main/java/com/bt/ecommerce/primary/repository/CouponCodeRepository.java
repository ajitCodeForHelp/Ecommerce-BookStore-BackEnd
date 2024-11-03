package com.bt.ecommerce.primary.repository;

import com.bt.ecommerce.primary.pojo.CouponCode;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CouponCodeRepository extends MongoRepository<CouponCode, ObjectId> {

    CouponCode findByUuid(String uuid);

    List<CouponCode> findByDeleted(boolean deleted);

    List<CouponCode> findByActive(boolean active);
}
