package com.bt.ecommerce.primary.service;

import com.bt.ecommerce.bean.DataTableResponsePacket;
import com.bt.ecommerce.bean.KeyValueDto;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.dto.AbstractDto;
import com.bt.ecommerce.primary.dto.CategoryDto;
import com.bt.ecommerce.primary.dto.DisplayCategoryDto;
import com.bt.ecommerce.primary.mapper.CategoryMapper;
import com.bt.ecommerce.primary.pojo.Category;
import com.bt.ecommerce.primary.pojo.Item;
import com.bt.ecommerce.primary.pojo.common.BasicParent;
import com.bt.ecommerce.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CategoryService extends _BaseService implements _BaseServiceImpl {

    @Override
    public String save(AbstractDto.Save save) throws BadRequestException {
        CategoryDto.SaveCategory saveCategoryDto = (CategoryDto.SaveCategory) save;
        Category category = CategoryMapper.MAPPER.mapToPojo(saveCategoryDto);
        if (!TextUtils.isEmpty(saveCategoryDto.getParentCategoryUuid())) {
            Category parentCategory = categoryRepository.findByUuid(saveCategoryDto.getParentCategoryUuid());
            category.setParentCategoryId(parentCategory.getId());
            category.setParentCategoryDetail(new BasicParent(parentCategory.getUuid(), parentCategory.getTitle()));
        }
        category = categoryRepository.save(category);
        return category.getUuid();
    }

    public String saveDisplayCategory(DisplayCategoryDto.SaveDisplayCategory saveDto) {
        Category category = CategoryMapper.MAPPER.mapToPojo(saveDto);
        category.setDisplayCategory(true);
        category = categoryRepository.save(category);
        return category.getUuid();
    }

    @Override
    public void update(String uuid, AbstractDto.Update update) throws BadRequestException {
        CategoryDto.UpdateCategory updateCategoryDto = (CategoryDto.UpdateCategory) update;
        Category category = findByUuid(uuid);
        if (!TextUtils.isEmpty(updateCategoryDto.getParentCategoryUuid())) {
            Category parentCategory = categoryRepository.findByUuid(updateCategoryDto.getParentCategoryUuid());
            category.setParentCategoryId(parentCategory.getId());
            category.setParentCategoryDetail(new BasicParent(parentCategory.getUuid(), parentCategory.getTitle()));
        } else {
            category.setParentCategoryId(null);
            category.setParentCategoryDetail(null);
        }
        category = CategoryMapper.MAPPER.mapToPojo(category, updateCategoryDto);
        categoryRepository.save(category);
    }

    public void updateDisplayCategory(String uuid, DisplayCategoryDto.UpdateDisplayCategory updateDto) throws BadRequestException {
        Category category = findByUuid(uuid);
        category = CategoryMapper.MAPPER.mapToPojo(category, updateDto);
        categoryRepository.save(category);
    }

    @Override
    public AbstractDto.Detail get(String uuid) throws BadRequestException {
        Category category = findByUuid(uuid);
        return mapToDetailDto(category);
    }

    private CategoryDto.DetailCategory mapToDetailDto(Category category) {
        CategoryDto.DetailCategory detailCategory = CategoryMapper.MAPPER.mapToDetailDto(category);
        List<Item> itemList = itemRepository.findByCategoryId(category.getId());
        if (!TextUtils.isEmpty(itemList)) {
            detailCategory.setCategoryAssignItems(itemList.stream()
                    .map(item -> new BasicParent(item.getUuid(), item.getTitle()))
                    .collect(Collectors.toList()));
        }
        return detailCategory;
    }

    @Override
    public DataTableResponsePacket list(Boolean deleted, Integer pageNumber, Integer pageSize, String search) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Category> pageData = categoryRepository.findByDeleted(deleted, search, pageable);
        return getDataTableResponsePacket(pageData, pageData.getContent().stream()
                .map(category -> CategoryMapper.MAPPER.mapToDetailDto(category))
                .collect(Collectors.toList()));
    }

    public List<CategoryDto.DetailCategory> list() {
        List<Category> list = categoryRepository.findAll();
        return list.stream()
                .map(category -> CategoryMapper.MAPPER.mapToDetailDto(category))
                .collect(Collectors.toList());
    }

    public DataTableResponsePacket listDisplayCategory(Boolean deleted, Integer pageNumber, Integer pageSize, String search) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Category> pageData = categoryRepository.findByDeletedAndDisplayCategory(deleted, search, pageable);
        return getDataTableResponsePacket(pageData, pageData.getContent().stream()
                .map(category -> CategoryMapper.MAPPER.mapToDetailDto(category))
                .collect(Collectors.toList()));
    }

    public List<CategoryDto.DetailCategory> listDisplayCategory() {
        List<Category> list = categoryRepository.findByDisplayCategory();
        return list.stream()
                .map(category -> CategoryMapper.MAPPER.mapToDetailDto(category))
                .collect(Collectors.toList());
    }

    @Override
    public void activate(String uuid) throws BadRequestException {
        Category category = findByUuid(uuid);
        category.setActive(true);
        category.setModifiedAt(LocalDateTime.now());
        categoryRepository.save(category);
    }

    @Override
    public void inactivate(String uuid) throws BadRequestException {
        Category category = findByUuid(uuid);
        category.setActive(false);
        category.setModifiedAt(LocalDateTime.now());
        categoryRepository.save(category);
    }

    @Override
    public void delete(String uuid) throws BadRequestException {
        Category category = findByUuid(uuid);
//        category.setDeleted(true);
//        category.setActive(false);
//        category.setModifiedAt(LocalDateTime.now());
        categoryRepository.delete(category);
    }

    @Override
    public void revive(String uuid) throws BadRequestException {
//        Category category = findByUuid(uuid);
//        category.setDeleted(false);
//        category.setModifiedAt(LocalDateTime.now());
//        categoryRepository.save(category);
    }

    public List<KeyValueDto> listInKeyValue() {
        List<Category> categoryList = categoryRepository.findByActiveAndDeleted();
        return categoryListInKeyValue(categoryList);
    }
    public List<KeyValueDto> listInKeyValueForDisplayCategory() {
        List<Category> categoryList = categoryRepository.findByActiveAndDeletedAndDisplayCategory();
        return categoryListInKeyValue(categoryList);
    }

    public void assignCategory(String uuid, List<String> itemUuids) throws BadRequestException {
        Category category = findByUuid(uuid);
        if (TextUtils.isEmpty(itemUuids)) {
            throw new BadRequestException("Invalid item assign, please check item Ids");
        }
        List<Item> itemList = itemRepository.findByUuids(itemUuids);
        if (TextUtils.isEmpty(itemList)) {
            throw new BadRequestException("Please provide valid item");
        }
        for (Item item : itemList) {
            // update parent category
            List<ObjectId> categoryIds = item.getCategoryIds();
            List<BasicParent> categoryDetails = item.getCategoryDetails();
            categoryIds.add(category.getId());
            categoryDetails.add(new BasicParent(category.getUuid(), category.getTitle()));
            item.setCategoryIds(categoryIds);
            item.setCategoryDetails(categoryDetails);
        }
        itemRepository.saveAll(itemList);
    }

    private List<KeyValueDto> categoryListInKeyValue(List<Category> categoryList) {
        Map<String, Category> parentCategoryMap = new HashMap<>();
        for (Category category : categoryList) {
            parentCategoryMap.put(category.getUuid(), category);
        }
        Map<String, Category> subCategoryMap = new HashMap<>();
        for (Category category : categoryList) {
            if(category.getParentCategoryId() != null){
                subCategoryMap.put(category.getUuid(), category);
                // Remove parent category and sub category
                parentCategoryMap.remove(category.getUuid());
                parentCategoryMap.remove(category.getParentCategoryDetail().getParentUuid());
            }
        }
        List<Category> categories = new ArrayList<>(parentCategoryMap.values().stream().toList());
        categories.addAll(subCategoryMap.values().stream().toList());
        return categories.stream()
                .map(category -> CategoryMapper.MAPPER.mapToKeyPairDto(category))
                .collect(Collectors.toList());
    }

    private Category findByUuid(String uuid) throws BadRequestException {
        Category category = categoryRepository.findByUuid(uuid);
        if (category == null) {
            throw new BadRequestException("ecommerce.common.message.record_not_exist");
        }
        return category;
    }

    private Category findById(ObjectId id) throws BadRequestException {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            throw new BadRequestException("ecommerce.common.message.record_not_exist");
        }
        return category;
    }
}