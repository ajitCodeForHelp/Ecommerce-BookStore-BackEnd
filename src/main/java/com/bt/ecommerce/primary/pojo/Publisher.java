package com.bt.ecommerce.primary.pojo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(value = "publisher")
public class Publisher extends _BasicEntity{
    private String title;
}
