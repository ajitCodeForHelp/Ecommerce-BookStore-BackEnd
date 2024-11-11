package com.bt.ecommerce.primary.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomerItemNotificationDto extends AbstractDto{
    @Getter
    @Setter
    public static class SaveCustomerItemNotification extends Save{
        private String itemId;
        private String customerIsdCode;
        private String customerMobile;
    }
    @Getter
    @Setter
    public static class DetailCustomerItemNotification extends Detail{
        private String itemId;
        private ItemDto.UpdateItem itemDetail;
        private String customerIsdCode;
        private String customerMobile;
    }
}
