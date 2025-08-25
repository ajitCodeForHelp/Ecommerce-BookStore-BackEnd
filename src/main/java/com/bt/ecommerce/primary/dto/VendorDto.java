package com.bt.ecommerce.primary.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VendorDto extends AbstractDto{
    @Getter
    @Setter
    public static class SaveVendor extends Save{
        private String title;
    }
    @Getter
    @Setter
    public static class UpdateVendor extends Update{
        private String title;
    }
    @Getter
    @Setter
    public static class DetailVendor extends Detail{
        private String title;
    }
}
