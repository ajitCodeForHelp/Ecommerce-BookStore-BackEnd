package com.bt.ecommerce.primary.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PublisherDto extends AbstractDto{
    @Getter
    @Setter
    public static class SavePublisher extends Save{
        private String title;
    }
    @Getter
    @Setter
    public static class UpdatePublisher extends Update{
        private String title;
    }
    @Getter
    @Setter
    public static class DetailPublisher extends Detail{
        private String title;
    }
}
