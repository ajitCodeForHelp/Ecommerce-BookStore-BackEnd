package com.bt.ecommerce.primary.repository;

import com.bt.ecommerce.primary.pojo.user.UserAdmin;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface UserAdminRepository extends MongoRepository<UserAdmin, ObjectId> {
    boolean existsByIsdCodeAndMobile(String isdCode, String mobile);

    boolean existsByEmail(String email);

    boolean existsByMobile(String phone);

    @Query(value = "{ $and  :[{'isd' : {'$eq' : ?0 }},{'mobile' : {'$eq' : ?1 }} ,{'id' : { '$ne' : ?2 }} ]}")
    UserAdmin findFirstByIsdAndMobileAndId(String isdCode, String mobile, ObjectId id);

    @Query(value = "{ $and  :[{'email' : {'$eq' : ?0 }},{'id' : {'$ne' : ?1 }} ]}")
    UserAdmin findFirstByEmailAndId(String email, ObjectId id);

    UserAdmin findByUuid(String uuid);

    UserAdmin findFirstByUsername(String username);

    List<UserAdmin> findByActiveAndDeleted(boolean active, boolean deleted);

    @Query(value = "{ " +
            "   $and : [" +
            "       { deleted    : { $eq : ?0} }," +
            "       { userType   : { $eq : 'ROLE_ADMIN'} }," +
            "       { $or : [ " +
            "           { firstName     : { $regularExpression : { pattern : ?1, options : 'i'} } }, " +
            "           { lastName      : { $regularExpression : { pattern : ?1, options : 'i'} } }, " +
            "           { mobile        : { $regularExpression : { pattern : ?1, options : 'i'} } }, " +
            "           { email         : { $regularExpression : { pattern : ?1, options : 'i'} } } " +
            "        ] }" +
            "   ]" +
            "}"
    )
    Page<UserAdmin> findByDeleted(boolean deleted, String search, Pageable pageable);

}
