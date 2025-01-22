package com.bt.ecommerce.primary.controller;

import com.bt.ecommerce.primary.service.*;
import com.bt.ecommerce.primary.userAccess.service.ModuleService;
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
    @Autowired protected WebsiteCmsSettingService websiteCmsSettingService;
    @Autowired protected CartService cartService;
    @Autowired protected FileManagerService fileManagerService;
    @Autowired protected EcommerceService ecommerceService;
    @Autowired protected CouponCodeService couponCodeService;
    @Autowired protected AddressService addressService;
    @Autowired protected ModuleService moduleService;
    @Autowired protected CustomerService customerService;
    @Autowired protected BannerService bannerService;
    @Autowired protected OrderService orderService;
    @Autowired protected DynamicFieldService dynamicFieldService;
    @Autowired protected StockInNotificationService stockInNotificationService;
    @Autowired protected OneTimePasswordService oneTimePasswordService;
    @Autowired protected SettingService settingService;
    @Autowired protected S3Service s3Service;
    @Autowired protected PublisherService publisherService;
    @Autowired protected TaxService taxService;
    @Autowired protected OrderHistoryService orderHistoryService;
    @Autowired protected CourierPartnerService courierPartnerService;

}