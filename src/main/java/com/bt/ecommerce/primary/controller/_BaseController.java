package com.bt.ecommerce.primary.controller;

import com.bt.ecommerce.primary.service.AuthService;
import com.bt.ecommerce.primary.service.CategoryService;
import com.bt.ecommerce.primary.service.ItemService;
import com.bt.ecommerce.primary.service.SystemUserService;
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




}