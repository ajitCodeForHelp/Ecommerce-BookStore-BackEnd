package com.bt.ecommerce.primary.controller;

import com.bt.ecommerce.primary.service.AuthService;
import com.bt.ecommerce.primary.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class _BaseController {

    @Autowired protected ShopService shopService;
    @Autowired protected AuthService authService;

//    @Autowired protected LocalCountryTrade localCountryTrade;
//    @Autowired protected LocalStateTrade localStateTrade;
//    @Autowired protected LocalCityTrade localCityTrade;


}