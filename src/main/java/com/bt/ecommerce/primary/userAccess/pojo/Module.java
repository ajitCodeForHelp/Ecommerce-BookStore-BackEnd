package com.bt.ecommerce.primary.userAccess.pojo;

import com.bt.ecommerce.primary.pojo._BasicEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Builder
@Document(collection = "module")
@CompoundIndexes({
        @CompoundIndex(name = "ci_module_title", def = "{'title' : 1}", unique = true)
})
public class Module extends _BasicEntity {

    private String title;

    @Getter
    @Setter
    public static class ModuleRef {
        private String uuid;
        private String title;
    }
}