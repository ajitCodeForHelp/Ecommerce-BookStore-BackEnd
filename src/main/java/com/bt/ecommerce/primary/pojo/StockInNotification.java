package com.bt.ecommerce.primary.pojo;

import com.bt.ecommerce.primary.pojo.common.BasicParent;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(value = "stock_in_notification")
public class StockInNotification extends _BasicEntity{

    private ObjectId itemId;
    private BasicParent itemDetails;

    private ObjectId customerId;

    private String customerIsdCode;
    private String customerMobile;
}
