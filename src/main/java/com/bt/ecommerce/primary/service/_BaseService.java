package com.bt.ecommerce.primary.service;

import com.bt.ecommerce.bean.DataTableResponsePacket;
import com.bt.ecommerce.primary.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.List;

public class _BaseService {
    @Autowired protected SystemUserRepository systemUserRepository;
    @Autowired protected CategoryRepository categoryRepository;
    @Autowired protected ItemRepository itemRepository;
    @Autowired protected CartRepository cartRepository;
    @Autowired protected AddressRepository addressRepository;
    @Autowired protected EcommerceJsonRepository ecommerceJsonRepository;
    @Autowired protected CouponCodeRepository couponCodeRepository;
    @Autowired protected BannerRepository bannerRepository;


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
