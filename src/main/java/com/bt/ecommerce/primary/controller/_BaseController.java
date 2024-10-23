package com.bt.ecommerce.primary.controller;

import com.bt.ecommerce.primary.pojo.EcommerceJson;
import com.bt.ecommerce.primary.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class _BaseController {

    @Autowired protected AuthService authService;
    @Autowired protected CategoryService categoryService;
    @Autowired protected ItemService itemService;
    @Autowired protected SystemUserService systemUserService;
    @Autowired protected CartService cartService;
    @Autowired protected FileManagerService fileManagerService;
    @Autowired protected EcommerceService ecommerceService;


}