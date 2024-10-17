package com.bt.ecommerce.primary.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CartDto extends AbstractDto{

    @Getter
    @Setter
    public static class CreateCart extends Save{
        private String addressUuid;
        private List<String> itemUuids;
        private String couponCodeUuid;
    }

    @Getter
    @Setter
    public static class UpdateCart extends Update{
        private List<String> itemUuids;
    }
}
