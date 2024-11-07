package com.bt.ecommerce.primary.pojo;


import com.bt.ecommerce.primary.pojo.enums.WebsiteCmsSettingEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Document(value = "website_cms_setting")
public class WebsiteCms extends _BasicEntity {

    private WebsiteCmsSettingEnum settingKey;
    private String settingValue;
}
