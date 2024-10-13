package com.bt.ecommerce.primary.pojo.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Document(collection = "customer")
public class Customer extends _BaseUser/*implements UserDetails*/ {


}

