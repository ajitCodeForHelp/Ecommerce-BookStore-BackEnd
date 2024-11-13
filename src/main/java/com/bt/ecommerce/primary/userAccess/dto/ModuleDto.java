package com.bt.ecommerce.primary.userAccess.dto;

import com.bt.ecommerce.primary.dto.AbstractDto;
import com.bt.ecommerce.primary.userAccess.pojo.Url;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class ModuleDto extends AbstractDto {

    @Setter
    @Getter
    public static class DetailModule extends Detail {
        private String title;
        private List<Url.UrlRef> urlList;
    }

    @Setter
    @Getter
    public static class SaveModule extends Save {
        @NotNull
        private String title;
    }

    @Setter
    @Getter
    public static class UpdateModule extends Update {
        @NotNull private String title;
    }

}