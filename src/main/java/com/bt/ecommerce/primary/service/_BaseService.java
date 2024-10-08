package com.bt.ecommerce.primary.service;

import com.bt.ecommerce.primary.repository.ShopRepository;
import com.bt.ecommerce.primary.repository.UserAdminRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class _BaseService {
    @Autowired protected UserAdminRepository userAdminRepository;
    @Autowired protected ShopRepository shopRepository;

}
