package com.bt.ecommerce.primary.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TaxDto extends AbstractDto{
    @Getter
    @Setter
    public static class SaveTax extends Save{
        private String title;
        private Double percentage;
    }
    @Getter
    @Setter
    public static class UpdateTax extends Update{
        private String title;
        private Double percentage;
    }
    @Getter
    @Setter
    public static class DetailTax extends Detail{
        private String title;
        private Double percentage;
    }
}
