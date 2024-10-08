package com.bt.ecommerce.primary.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ShopDto {


    @Setter
    @Getter
    public static class Save {
        private String orderPreFix;
        private String title;

//        private Address address;

        private String gstNo;
        private String vatNo;

        private String shopImageUrl;

        private ShopOwnerDto shopOwnerDto;

    }


}
