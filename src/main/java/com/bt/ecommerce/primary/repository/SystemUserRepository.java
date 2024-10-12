package com.bt.ecommerce.primary.repository;

import com.bt.ecommerce.primary.pojo.user.SystemUser;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface SystemUserRepository extends MongoRepository<SystemUser, ObjectId> {
    boolean existsByIsdCodeAndMobile(String isdCode, String mobile);

    boolean existsByEmail(String email);

    boolean existsByMobile(String phone);

    @Query(value = "{ $and  :[{'isd' : {'$eq' : ?0 }},{'mobile' : {'$eq' : ?1 }} ,{'id' : { '$ne' : ?2 }} ]}")
    SystemUser findFirstByIsdAndMobileAndId(String isdCode, String mobile, ObjectId id);

    @Query(value = "{ $and  :[{'email' : {'$eq' : ?0 }},{'id' : {'$ne' : ?1 }} ]}")
    SystemUser findFirstByEmailAndId(String email, ObjectId id);

    SystemUser findByUuid(String uuid);

    SystemUser findFirstByUsername(String username);

    @Query(value = "{ $and  :[" +
            "{'active'   : {'$eq' : true }}," +
            "{'deleted'  : {'$eq' : false }}," +
            "{'userType' : { $eq : 'ROLE_SUB_ADMIN'}} ]}")
    List<SystemUser> findByActiveAndDeleted();

    @Query(value = "{ " +
            "   $and : [" +
            "       { deleted    : { $eq : ?0} }," +
            "       { userType   : { $eq : 'ROLE_SUB_ADMIN'} }," +
            "       { $or : [ " +
            "           { firstName  : { $regularExpression : { pattern : ?1, options : 'i'} } }, " +
            "           { lastName   : { $regularExpression : { pattern : ?1, options : 'i'} } }, " +
            "           { mobile     : { $regularExpression : { pattern : ?1, options : 'i'} } }, " +
            "           { jobRole    : { $regularExpression : { pattern : ?1, options : 'i'} } }, " +
            "           { email      : { $regularExpression : { pattern : ?1, options : 'i'} } } " +
            "        ] }" +
            "   ]" +
            "}"
    )
    Page<SystemUser> findByDeleted(boolean deleted, String search, Pageable pageable);

}
