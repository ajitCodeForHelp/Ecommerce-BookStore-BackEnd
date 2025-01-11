package com.bt.ecommerce.primary.repository;

import com.bt.ecommerce.primary.pojo.CouponCode;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface CouponCodeRepository extends MongoRepository<CouponCode, ObjectId> {

    CouponCode findByUuid(String uuid);

    CouponCode findByCouponCode(String couponCode);

    List<CouponCode> findByDeleted(boolean deleted);

    @Query(value = "{" +
            "  'active'  : { '$eq' : true }," +
            "  'endDate' : { '$gt' : ?0 }" +
            "  'referralCoupon' : { '$eq' : false }" +
            "}")
    List<CouponCode> findActiveCouponCodeList(LocalDate localDate);

    List<CouponCode> findByActiveAndDeleted(boolean active, boolean deleted);
}
