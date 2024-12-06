package com.bt.ecommerce.primary.service;

import com.bt.ecommerce.primary.bean.EcommerceBean;
import com.bt.ecommerce.primary.mapper.EcommerceMapper;
import com.bt.ecommerce.primary.pojo.Category;
import com.bt.ecommerce.primary.pojo.EcommerceJson;
import com.bt.ecommerce.primary.pojo.Item;
import com.bt.ecommerce.utils.ProjectConst;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class EcommerceDataService extends _BaseService {

    public void generateEcommerceDefaultData() {
        generateHomepageData();
        generateAllCategoryDetailJsonData();
    }

    private void generateHomepageData() {
        EcommerceBean.HomepageData homepageData = new EcommerceBean.HomepageData();
        List<EcommerceBean.DashboardCategory> dashboardCategoryList = dashboardCategory();
        List<EcommerceBean.EcommerceCategory> displayCategoryList = getAllDisplayCategory();

        // Set Dashboard Category Data To Homepage
        homepageData.setDashboardCategoryList(dashboardCategoryList);
        homepageData.setDisplayCategoryList(displayCategoryList);

        // Prepare Ecommerce Json Data
        EcommerceJson ecommerceJson = ecommerceJsonRepository.findByKey(ProjectConst.HOMEPAGE_DATA);
        if (ecommerceJson == null) {
            ecommerceJson = new EcommerceJson();
            ecommerceJson.setKey(ProjectConst.HOMEPAGE_DATA);
        }
        ecommerceJson.setValue(new Gson().toJson(homepageData));
        ecommerceJsonRepository.save(ecommerceJson);
    }

    private List<EcommerceBean.DashboardCategory> dashboardCategory() {
        List<Category> categoryList = categoryRepository.findByActiveAndDeleted();
        Map<String, Category> categoryMap = new HashMap<>();
        // <ParentCategoryUuid, List<SubCategory>
        Map<String, List<Category>> subCategoryMap = new HashMap<>();
        for (Category category : categoryList) {
            // If Category Doesn't Have Any Item Then Skip This
//            if(itemRepository.findByCategoryId(category.getId()).size() == 0) continue;
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

    private List<EcommerceBean.EcommerceCategory> getAllDisplayCategory() {
        List<Category> displayCategoryList = categoryRepository.findByActiveAndDeletedAndDisplayCategory();
        // Prepare EcommerceCategory For HomePage
        List<EcommerceBean.EcommerceCategory> ecommerceCategoryList = new ArrayList<>();
        for (Category displayCategory : displayCategoryList) {
//            if(itemRepository.findByCategoryId(displayCategory.getId()).size() == 0) continue;
            ecommerceCategoryList.add(getEcommerceCategoryDetail(displayCategory));
        }
        return ecommerceCategoryList;
    }

    private void generateAllCategoryDetailJsonData() {
        // Get All Active | Non-Deleted Category
        List<Category> categoryList = categoryRepository.findByAllCategory();
        for (Category category : categoryList) {
            EcommerceBean.EcommerceCategory ecommerceCategoryDetail = getEcommerceCategoryDetail(category);
            // Prepare Ecommerce Json Data
            EcommerceJson ecommerceJson = ecommerceJsonRepository.findByKey(category.getUuid());
            if (ecommerceJson == null) {
                ecommerceJson = new EcommerceJson();
                ecommerceJson.setKey(category.getUuid());
            }
            ecommerceJson.setValue(new Gson().toJson(ecommerceCategoryDetail));
            ecommerceJsonRepository.save(ecommerceJson);
        }
    }

    private EcommerceBean.EcommerceCategory getEcommerceCategoryDetail(Category category) {
        EcommerceBean.EcommerceCategory ecommerceCategory = EcommerceMapper.MAPPER.mapToEcommerceCategory(category);
        List<Item> itemList = itemRepository.findByCategoryId(category.getId());
        // Prepare EcommerceCategoryItem For HomePage
        List<EcommerceBean.EcommerceCategoryItem> ecommerceCategoryItemList = new ArrayList<>();
        for (Item item : itemList) {
            ecommerceCategoryItemList.add(EcommerceMapper.MAPPER.mapToEcommerceCategoryItem(item));
        }
        ecommerceCategory.setDisplayCategoryItemList(ecommerceCategoryItemList);
        return ecommerceCategory;
    }
}