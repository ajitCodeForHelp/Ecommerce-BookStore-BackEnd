package com.bt.ecommerce.primary.pojo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(value = "courier_partner")
public class CourierPartner extends _BasicEntity{
    private String title;
    private String trackingUrl;
}
