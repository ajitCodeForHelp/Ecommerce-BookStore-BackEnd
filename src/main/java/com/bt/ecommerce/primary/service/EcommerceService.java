package com.bt.ecommerce.primary.service;

import com.bt.ecommerce.primary.bean.EcommerceBean;
import com.bt.ecommerce.primary.pojo.Category;
import com.bt.ecommerce.primary.pojo.EcommerceJson;
import com.bt.ecommerce.utils.Const;
import com.bt.ecommerce.utils.ProjectConst;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;

@Slf4j
@Service
public class EcommerceService extends _BaseService {

    public EcommerceBean.HomepageData getHomepageData() {
        EcommerceJson ecommerceJson = ecommerceJsonRepository.findByKey(ProjectConst.HOMEPAGE_DATA);
        if (ecommerceJson == null) return null;
        Type type = new TypeToken<EcommerceBean.HomepageData>() {
        }.getType();
        EcommerceBean.HomepageData homepageData = new Gson().fromJson(ecommerceJson.getValue(), type);
        return homepageData;
    }

    public EcommerceBean.EcommerceCategory getCategoryItemDetails(String categoryUuid) {
        Category category = categoryRepository.findByUuid(categoryUuid);
        EcommerceJson ecommerceJson = ecommerceJsonRepository.findByKey(category.getUuid());
        if (ecommerceJson == null) return null;
        Type type = new TypeToken<EcommerceBean.EcommerceCategory>() {
        }.getType();
        return new Gson().fromJson(ecommerceJson.getValue(), type);
    }


    public EcommerceBean.ContactAndSocial contactAndSocialDetail() {
        EcommerceBean.ContactAndSocial contactAndSocial = new EcommerceBean.ContactAndSocial();
//        contactAndSocial.setSupportContactNo(Const.SystemSetting.);
        return null;
    }


}