//package com.kpis.kpisFood.primary.repository;
//
//import com.kpis.kpisFood.primary.pojo.client.Brand;
//import org.bson.types.ObjectId;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.mongodb.repository.Query;
//
//import java.util.List;
//
//public interface BrandRepository extends AbstractRepository<Brand> {
//
//    List<Brand> findByClientIdAndDeleted(ObjectId clientId, Boolean deleted);
//
//    @Query(value = "" +
//            "{" +
//            "  'clientId'  : { '$eq' : ?0 }," +
//            "  'deleted'    : { '$eq' : ?1 }, " +
//            "   $or : [" +
//            "       { 'brandTitle'        : { $regex : ?2, $options : 'i' } }, " +
//            "       { 'brandDescription'  : { $regex : ?2, $options : 'i' } } " +
//            "   ]" +
//            "}")
//    Page<Brand> findByClientIdAndDeleted(ObjectId clientId, boolean deleted, String search, Pageable pageable);
//
//    @Query(value = "" +
//            "{" +
//            "  'clientId'  : { '$eq' : ?0 }," +
//            "  'uuid'      : { '$eq' : ?1 }," +
//            "}")
//    Brand findByClientIdAndUuid(ObjectId clientId, String uuid);
//}