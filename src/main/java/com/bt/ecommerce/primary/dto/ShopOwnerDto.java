package com.bt.ecommerce.primary.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShopOwnerDto {
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String pwdText;
    protected String photoImageUrl;
    protected String mobile;
}
