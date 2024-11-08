package com.bt.ecommerce.primary.userAccess.repository;

import com.bt.ecommerce.primary.userAccess.pojo.Module;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ModuleRepository extends MongoRepository<Module, ObjectId> {

    Module findByTitle(String title);

    @Query(value = "" +
            "{ " +
            "   'title' : { $eq : ?0 }, " +
            "   '_id'   : { $ne : ?1}, " +
            "}")
    Module findByTitleAndNotId(String title, ObjectId id);

    @Query(value = "" +
            "{ " +
            "   'deleted' : { $eq : false }, " +
            "   'active'  : { $eq : true}, " +
            "}")
    List<Module> findAllModuleList();

    @Query(value = "" +
            "{ " +
            "  '_uuid'  : {'$in' : ?0 }, " +
            "  'deleted' : { $eq : false }, " +
            "  'active'  : { $eq : true} " +
            "}")
    List<Module> findByUuids(List<String> uuids);

    Module findByUuid(String uuid);

    List<Module> findByActiveAndDeleted(boolean active, boolean deleted);

    List<Module> findByDeleted(boolean deleted);
}