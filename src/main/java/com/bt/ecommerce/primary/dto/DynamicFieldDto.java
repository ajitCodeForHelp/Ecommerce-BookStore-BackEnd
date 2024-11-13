package com.bt.ecommerce.primary.dto;

import com.bt.ecommerce.primary.pojo.common.BasicParent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DynamicFieldDto extends AbstractDto{
    @Getter
    @Setter
    public static class SaveDynamicField extends Save{
        private String title;
        private String Type;
    }
    @Getter
    @Setter
    public static class UpdateDynamicField extends Update{
        private String title;
        private String Type;
    }
    @Getter
    @Setter
    public static class DetailDynamicField extends Detail{
        private String title;
        private String Type;
    }
}
