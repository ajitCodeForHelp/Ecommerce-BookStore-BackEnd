package com.bt.ecommerce.primary.pojo;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
public abstract class _BasicEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @MongoId
    @Field("_id")
    protected ObjectId id;

    @Field("_uuid")
    protected String uuid = UUID.randomUUID().toString();

    protected boolean active = true;

    @CreatedDate
    protected LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    protected LocalDateTime modifiedAt = LocalDateTime.now();

    protected boolean deleted = false;

}
