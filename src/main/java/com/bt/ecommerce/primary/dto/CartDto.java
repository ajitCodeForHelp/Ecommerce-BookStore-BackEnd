package com.bt.ecommerce.primary.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CartDto extends AbstractDto{

    @Getter
    @Setter
    public static class createCart extends Save{
        private String addressUuid;
        private List<String> itemUuids;
        private String couponCodeUuid;
    }
}
