package com.bt.ecommerce.primary.service;

import com.bt.ecommerce.primary.bean.EcommerceBean;
import com.bt.ecommerce.primary.mapper.EcommerceMapper;
import com.bt.ecommerce.primary.pojo.Category;
import com.bt.ecommerce.primary.pojo.EcommerceJson;
import com.bt.ecommerce.primary.pojo.Item;
import com.bt.ecommerce.utils.ProjectConst;
import com.bt.ecommerce.utils.TextUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class EcommerceService extends _BaseService {

    @PostConstruct
    public EcommerceJson generateHomepageData() {
        EcommerceBean.HomepageData homepageData = new EcommerceBean.HomepageData();
        List<EcommerceBean.DashboardCategory> dashboardCategoryList = dashboardCategory();
        List<EcommerceBean.DisplayCategory> displayCategoryList = displayCategory();

        // Set Dashboard Category Data To Homepage
        homepageData.setDashboardCategoryList(dashboardCategoryList);
        homepageData.setDisplayCategoryList(displayCategoryList);

        // Prepare Ecommerce Json Data
        EcommerceJson ecommerceJson = new EcommerceJson();
        ecommerceJson.setKey(ProjectConst.HOMEPAGE_DATA);
        ecommerceJson.setValue(new Gson().toJson(homepageData));
        ecommerceJson = ecommerceJsonRepository.save(ecommerceJson);
        return ecommerceJson;
    }

    public EcommerceBean.HomepageData getHomepageData() {
        EcommerceJson ecommerceJson = ecommerceJsonRepository.findByKey(ProjectConst.HOMEPAGE_DATA);
        if(ecommerceJson == null){
            ecommerceJson = generateHomepageData();
        }
        Type type = new TypeToken<EcommerceBean.HomepageData>() {}.getType();
        EcommerceBean.HomepageData homepageData = new Gson().fromJson(ecommerceJson.getValue(), type);
        return homepageData;
    }

    private List<EcommerceBean.DashboardCategory> dashboardCategory() {
        List<Category> categoryList = categoryRepository.findByActiveAndDeleted();
        Map<String, Category> categoryMap = new HashMap<>();
        // <ParentCategoryUuid, List<SubCategory>
        Map<String, List<Category>> subCategoryMap = new HashMap<>();
        // TODO >> Remove Those Which Are Not Having Any Item
        for (Category category : categoryList) {
            if (category.getParentCategoryId() == null) {
                categoryMap.put(category.getUuid(), category);
            } else {
                if (!subCategoryMap.containsKey(category.getParentCategoryDetail().getParentUuid())) {
                    subCategoryMap.put(category.getParentCategoryDetail().getParentUuid(), new ArrayList<>());
                }
                List<Category> subcategoryList = subCategoryMap.get(category.getParentCategoryDetail().getParentUuid());
                subcategoryList.add(category);
                subCategoryMap.put(category.getParentCategoryDetail().getParentUuid(), subcategoryList);
            }
        }

        // Prepare DashBoard Category
        List<EcommerceBean.DashboardCategory> dashboardCategoryList = new ArrayList<>();
        if (categoryMap.isEmpty()) return null;
        for (String categoryUuid : categoryMap.keySet()) {
            Category category = categoryMap.get(categoryUuid);
            EcommerceBean.DashboardCategory dashboardCategory = EcommerceMapper.MAPPER.mapToDashboardCategory(category);

            // Check if their any sub-category exist
            if (subCategoryMap.containsKey(category.getUuid())) {
                List<EcommerceBean.DashboardCategory> dashboardSubCategoryList = new ArrayList<>();
                for (Category subCategory : subCategoryMap.get(category.getUuid())) {
                    dashboardSubCategoryList.add(EcommerceMapper.MAPPER.mapToDashboardCategory(subCategory));
                }
                dashboardCategory.setSubCategoryList(dashboardSubCategoryList);
            }
            dashboardCategoryList.add(dashboardCategory);
        }

        return dashboardCategoryList;
    }

    private List<EcommerceBean.DisplayCategory> displayCategory() {
        List<Category> categoryList = categoryRepository.findByActiveAndDeletedAndDisplayCategory();
        // Prepare DisplayCategory For HomePage
        List<EcommerceBean.DisplayCategory> displayCategoryList = new ArrayList<>();
        for (Category category : categoryList) {
            EcommerceBean.DisplayCategory displayCategory = EcommerceMapper.MAPPER.mapToDisplayCategory(category);
            List<Item> itemList = itemRepository.findByCategoryId(category.getId());
            if(TextUtils.isEmpty(itemList)) continue;
            // Prepare DisplayCategoryItem For HomePage
            List<EcommerceBean.DisplayCategoryItem> displayCategoryItemList = new ArrayList<>();
            for (Item item : itemList) {
                displayCategoryItemList.add(EcommerceMapper.MAPPER.mapToDisplayCategoryItem(item));
            }
            displayCategory.setDisplayCategoryItemList(displayCategoryItemList);
            displayCategoryList.add(displayCategory);
        }
        return displayCategoryList;
    }

}