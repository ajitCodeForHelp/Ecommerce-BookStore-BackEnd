package com.bt.ecommerce.primary.repository;

import com.bt.ecommerce.primary.pojo.Category;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface CategoryRepository extends MongoRepository<Category, ObjectId> {

    Category findByUuid(String uuid);
    @Query(value = "" +
            "{" +
            "  '_uuid'  : { '$in' : ?0 }," +
            "}")
    List<Category> findByUuids(List<String> uuids);
    @Query(value = "" +
            "{" +
            "  '_id'  : { '$in' : ?0 }," +
            "}")
    List<Category> findByIds(List<ObjectId> ids);

    @Query(value = "" +
            "{" +
            "  'parentCategoryId'  : { '$in' : ?0 }," +
            "}")
    List<Category> findByParentCategoryIds(List<ObjectId> parentCategoryIds);
    @Query(value = "" +
            "{" +
            "  'active'  : { '$eq' : true }," +
            "  'deleted' : { '$eq' : false }," +
            "  'displayCategory' : { '$ne' : true }," +
            "}")
    List<Category> findByActiveAndDeleted();

    @Query(value = "" +
            "{" +
            "  'active'  : { '$eq' : true }," +
            "  'deleted' : { '$eq' : false }," +
            "  'displayCategory' : { '$eq' : true }," +
            "}")
    List<Category> findByActiveAndDeletedAndDisplayCategory();

    @Query(value = "" +
            "{" +
            "  'active'  : { '$eq' : true }," +
            "  'deleted' : { '$eq' : false }," +
            "  'parentCategoryId' : { '$eq' : ?0 }," +
            "}")
    List<Category> getCategoryList(ObjectId parentCategoryId);

    @Query(value = "{ " +
            "   $and : [" +
            "       { deleted    : { $eq : ?0} }," +
            "       { displayCategory  : { $ne : true} }," +
            "       { $or : [ " +
            "           { title : { $regularExpression : { pattern : ?1, options : 'i'} } }" +
            "        ] }" +
            "   ]" +
            "}"
    )
    Page<Category> findByDeleted(boolean deleted, String search, Pageable pageable);

    @Query(value = "{ " +
            "   $and : [" +
            "       { deleted  : { $eq : ?0} }," +
            "       { displayCategory  : { $eq : true} }," +
            "       { $or : [ " +
            "           { title : { $regularExpression : { pattern : ?1, options : 'i'} } }" +
            "        ] }" +
            "   ]" +
            "}"
    )
    Page<Category> findByDeletedAndDisplayCategory(boolean deleted, String search, Pageable pageable);
}
