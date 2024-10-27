package com.bt.ecommerce.primary.repository;

import com.bt.ecommerce.primary.pojo.Banner;
import com.bt.ecommerce.primary.pojo.Cart;
import com.bt.ecommerce.primary.pojo.Category;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface BannerRepository extends MongoRepository<Banner, ObjectId> {
    Banner findByUuid(String uuid);
    @Query(value = "{ " +
            "   $and : [" +
            "       { deleted    : { $eq : ?0} }," +
            "       { $or : [ " +
            "           { title : { $regularExpression : { pattern : ?1, options : 'i'} } }" +
            "        ] }" +
            "   ]" +
            "}"
    )
    Page<Banner> findByDeleted(boolean deleted, String search, Pageable pageable);

}
