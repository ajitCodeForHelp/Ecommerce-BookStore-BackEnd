package com.bt.ecommerce.primary.pojo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(value = "customer_item_notification")
public class CustomerItemNotification extends _BasicEntity{
    private String itemId;
    private String customerIsdCode;
    private String customerMobile;
}
