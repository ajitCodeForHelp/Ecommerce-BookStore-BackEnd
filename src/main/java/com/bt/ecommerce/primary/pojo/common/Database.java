package com.bt.ecommerce.primary.pojo.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Database {

    private String databaseUri;
    private String databaseName;
}
