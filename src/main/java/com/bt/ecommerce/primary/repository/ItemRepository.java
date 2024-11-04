package com.bt.ecommerce.primary.repository;

import com.bt.ecommerce.primary.pojo.Item;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ItemRepository extends MongoRepository<Item, ObjectId> {

    Item findByUuid(String uuid);

    @Query(value = "" +
            "{" +
            "  '_uuid'  : { '$in' : ?0 }," +
            "}")
    List<Item> findByUuids(List<String> itemUuids);
    @Query(value = "" +
            "{" +
            "  'parentCategoryIds'  : { '$in' : [?0] }," +
            "}")
    List<Item> findByCategoryId(ObjectId categoryId);

    @Query(value = "" +
            "{" +
            "  'active'  : { '$eq' : true }," +
            "  'deleted' : { '$eq' : false }," +
            "}")
    List<Item> findByActiveAndDeleted();

    @Query(value = "" +
            "{" +
            "  'active'  : { '$eq' : true }," +
            "  'deleted' : { '$eq' : false }," +
            "  'parentCategoryId' : { '$eq' : ?0 }," +
            "}")
    List<Item> getCategoryList(ObjectId parentCategoryId);

    @Query(value = "{ " +
            "   $and : [" +
            "       { deleted    : { $eq : ?0} }," +
            "       { $or : [ " +
            "           { title : { $regularExpression : { pattern : ?1, options : 'i'} } }" +
            "        ] }" +
            "   ]" +
            "}"
    )
    Page<Item> findByDeleted(boolean deleted, String search, Pageable pageable);
}
