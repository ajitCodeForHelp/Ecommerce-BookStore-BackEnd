package com.bt.ecommerce.primary.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class   _BasicDto {


    protected String uuid;
    protected Boolean active;
    protected Long createdAtTimeStamp;
    protected Long modifiedAtTimeStamp;
    protected Boolean deleted;

}
