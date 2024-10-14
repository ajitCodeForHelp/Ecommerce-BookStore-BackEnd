package com.bt.ecommerce.primary.repository;

import com.bt.ecommerce.primary.pojo.CouponCode;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CouponCodeRepository extends MongoRepository<CouponCode, ObjectId> {
}
