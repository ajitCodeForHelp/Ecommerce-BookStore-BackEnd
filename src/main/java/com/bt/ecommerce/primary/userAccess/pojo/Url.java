package com.bt.ecommerce.primary.userAccess.pojo;

import com.bt.ecommerce.primary.pojo._BasicEntity;
import com.bt.ecommerce.primary.pojo.common.BasicParent;
import com.bt.ecommerce.primary.userAccess.pojo.enums.AccessTypeEnum;
import com.bt.ecommerce.primary.userAccess.pojo.enums.MethodTypeEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;


@Setter
@Getter
@Builder
@Document(collection = "url")
@CompoundIndexes({
        @CompoundIndex(name = "ci_url_title", def = "{'title' : 1}", unique = true)
})
public class Url extends _BasicEntity {

    private ObjectId moduleId;
    private BasicParent moduleDetail;

    private String url;
    private String title;
    private String description;
    private MethodTypeEnum methodType;
    private AccessTypeEnum accessType;

    @Getter
    @Setter
    public static class UrlRef {
        private String uuid;
        protected boolean active;
        private String title;
        private String url;
    }
}