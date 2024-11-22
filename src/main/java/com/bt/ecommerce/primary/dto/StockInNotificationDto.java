package com.bt.ecommerce.primary.dto;

import com.bt.ecommerce.primary.pojo.common.BasicParent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StockInNotificationDto extends AbstractDto{
    @Getter
    @Setter
    public static class SaveItemNotification extends Save{
        @NotNull private String itemUuid;
        // Delivery Person Details
        @NotNull private String customerIsdCode;
        @NotNull private String customerMobile;
    }
    @Getter
    @Setter
    public static class DetailCustomerItemNotification extends Detail{
        private BasicParent itemDetails;
        private String customerMobile;
        private Boolean notifiedToCustomer;
    }
}
