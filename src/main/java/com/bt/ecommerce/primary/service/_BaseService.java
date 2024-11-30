package com.bt.ecommerce.primary.service;

import com.bt.ecommerce.bean.DataTableResponsePacket;
import com.bt.ecommerce.primary.repository.*;
import com.bt.ecommerce.primary.userAccess.repository.ModuleRepository;
import com.bt.ecommerce.primary.userAccess.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.List;

public class _BaseService {
    @Autowired protected SystemUserRepository systemUserRepository;
    @Autowired protected WebsiteCmsSettingRepository websiteCmsSettingRepository;
    @Autowired protected CategoryRepository categoryRepository;
    @Autowired protected ItemRepository itemRepository;
    @Autowired protected CartRepository cartRepository;
    @Autowired protected AddressRepository addressRepository;
    @Autowired protected EcommerceJsonRepository ecommerceJsonRepository;
    @Autowired protected CouponCodeRepository couponCodeRepository;
    @Autowired protected CustomerRepository customerRepository;
    @Autowired protected BannerRepository bannerRepository;
    @Autowired protected OrderRepository orderRepository;

    @Autowired protected DynamicFieldRepository dynamicFieldRepository;
    @Autowired protected StockInNotificationRepository stockInNotificationRepository;
    @Autowired protected OneTimePasswordRepository oneTimePasswordRepository;

    @Autowired protected SettingRepository settingRepository;
    @Autowired protected ModuleRepository moduleRepository;
    @Autowired protected UrlRepository urlRepository;
    @Autowired protected PaymentTransactionRepository paymentTransactionRepository;

    @Autowired protected PublisherRepository publisherRepository;

    @Autowired protected TaxRepository taxRepository;




    protected DataTableResponsePacket getDataTableResponsePacket(Page pageData, List data) {
        DataTableResponsePacket responsePacket = new DataTableResponsePacket();
        responsePacket.setCurrentPage(pageData.getNumber());
        responsePacket.setPageSize(pageData.getPageable().getPageSize());
        responsePacket.setTotalPages(pageData.getTotalPages());
        responsePacket.setTotalItems(pageData.getTotalElements());
        responsePacket.setData(data);
        return responsePacket;
    }

}
