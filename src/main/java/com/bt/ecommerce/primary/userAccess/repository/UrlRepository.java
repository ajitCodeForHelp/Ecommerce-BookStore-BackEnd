package com.bt.ecommerce.primary.userAccess.repository;

import com.bt.ecommerce.primary.userAccess.pojo.Url;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface UrlRepository extends MongoRepository<Url, ObjectId> {
    Url findByTitle(String title);

    List<Url> findByModuleId(ObjectId moduleId);

    Url findByUuid(String urlUuid);

    @Query(value = "{" +
            " '_uuid'    : {'$in' : ?0 } " +
            "}")
    List<Url> findByUuids(List<String> urlUuidList);

    List<Url> findByModuleIdAndActive(ObjectId moduleId, boolean active);
}