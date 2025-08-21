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
        String existingParentUuid = category.getParentCategoryDetail() != null
                ? category.getParentCategoryDetail().getParentUuid()
                : null;
        String newParentUuid = updateCategoryDto.getParentCategoryUuid();
        if (!Objects.equals(existingParentUuid, newParentUuid)) {
            List<Item> itemList = itemRepository.findByCategoryId(category.getId());
            for (Item item : itemList) {
                boolean subCategoryUpdated = true;
                for (BasicParent basicParent : item.getParentCategoryDetails()) {
                    if (basicParent.getParentUuid().equals(uuid)) {
                        if (updateCategoryDto.getParentCategoryUuid() != null) {
                            Category newParentCategory = categoryRepository.findByUuid(updateCategoryDto.getParentCategoryUuid());
                            if (!item.getParentCategoryIds().contains(newParentCategory.getId())) {
                                basicParent.setParentUuid(newParentCategory.getUuid());
                                basicParent.setParentTitle(newParentCategory.getTitle());
                                item.getParentCategoryIds().add(newParentCategory.getId());
                            }
                            item.getParentCategoryIds().remove(category.getId());
                            item.getSubCategoryIds().add(category.getId());
                            item.getSubCategoryDetails().add(new BasicParent(category.getUuid(), category.getTitle()));
                            itemRepository.save(item);
                            subCategoryUpdated = false;
                        }
                    }
                }

                //For Sub-Category
                List<BasicParent> subCategoryCopy = new ArrayList<>(item.getSubCategoryDetails());
                if (subCategoryUpdated) {
                    for (BasicParent basicParent : subCategoryCopy) {
                        if (basicParent.getParentUuid().equals(uuid)) {
                            if (updateCategoryDto.getParentCategoryUuid() == null) {
                                item.getSubCategoryDetails().remove(basicParent);
                                item.getSubCategoryIds().remove(uuid);
                                item.getParentCategoryIds().remove(category.getParentCategoryId());
                                item.getParentCategoryDetails().remove(category.getParentCategoryDetail());
                                if (!item.getParentCategoryIds().contains(category.getId())) {
                                    item.getParentCategoryIds().add(category.getId());
                                    item.getParentCategoryDetails().add(new BasicParent(category.getUuid(), category.getTitle()));
                                }
                                itemRepository.save(item);
                            }
                            if (updateCategoryDto.getParentCategoryUuid() != null &&
                                    !(category.getParentCategoryDetail() != null && category.getParentCategoryDetail().getParentUuid().equals(updateCategoryDto.getParentCategoryUuid()))) {
                                item.getParentCategoryDetails().remove(category.getParentCategoryDetail());
                                item.getParentCategoryIds().remove(category.getParentCategoryId());
                                Category newParentCategory = categoryRepository.findByUuid(updateCategoryDto.getParentCategoryUuid());
                                if (!item.getParentCategoryIds().contains(newParentCategory.getId())) {
                                    item.getParentCategoryIds().add(newParentCategory.getId());
                                    item.getParentCategoryDetails().add(new BasicParent(newParentCategory.getUuid(), newParentCategory.getTitle()));
                                }
                                itemRepository.save(item);
                            }
                        }
                    }
                }
            }
        }

        if (!TextUtils.isEmpty(updateCategoryDto.getParentCategoryUuid())) {
            Category parentCategory = categoryRepository.findByUuid(updateCategoryDto.getParentCategoryUuid());
            category.setParentCategoryId(parentCategory.getId());
            category.setParentCategoryDetail(new BasicParent(parentCategory.getUuid(), parentCategory.getTitle()));
        } else {
            category.setParentCategoryId(null);
            category.setParentCategoryDetail(null);
        }

        boolean needToUpdateItemCategory = false;
        if(!category.getTitle().equalsIgnoreCase(updateCategoryDto.getTitle())
        ){
            needToUpdateItemCategory = true;
        }
        category = CategoryMapper.MAPPER.mapToPojo(category, updateCategoryDto);
        categoryRepository.save(category);
        if(needToUpdateItemCategory){
            updateParentCategoryDetailInSubCategory(category);
            updateCategoryDetailInItem(category);
        }
    }
    public void updateCategoryDetailInItem(Category category) {
        List<Item> affectedItems = itemRepository.findByCategoryId(category.getId());

        for (Item item : affectedItems) {
            boolean updated = false;

            for (BasicParent parent : item.getParentCategoryDetails()) {
                if (parent.getParentUuid().equals(category.getUuid())) {
                    parent.setParentTitle(category.getTitle());
                    parent.setParentUuid(category.getUuid());
                    updated = true;
                }
            }

            for (BasicParent sub : item.getSubCategoryDetails()) {
                if (sub.getParentUuid().equals(category.getUuid())) {
                    sub.setParentTitle(category.getTitle());
                    sub.setParentUuid(category.getUuid());
                    updated = true;
                }
            }

            if (updated) {
                itemRepository.save(item);
            }
        }
    }


    private void updateParentCategoryDetailInSubCategory(Category category){
        List<Category> categoryList = categoryRepository.findByParentCategoryId(category.getId());
        if(TextUtils.isEmpty(categoryList)) return;
        for (Category category1 : categoryList) {
            category1.setParentCategoryDetail(new BasicParent(category.getUuid(),category.getTitle()));
        }
        categoryRepository.saveAll(categoryList);
    }

    public void reorderingCatSubCatSequence(CategoryDto.CatSubCatSequenceReorder catSubCatSequence) {
        for (CategoryDto.CatSubCatSequence sequence : catSubCatSequence.getCatSubCatSequences()) {
            Category category = categoryRepository.findByUuid(sequence.getId());
            category.setSequenceNo(sequence.getSequenceNo());
            categoryRepository.save(category);
        }
    }
    public void updateDisplayCategory(String uuid, DisplayCategoryDto.UpdateDisplayCategory updateDto) throws BadRequestException {
        Category category = findByUuid(uuid);
        boolean needToUpdateItemCategory = false;
        if(!category.getTitle().equalsIgnoreCase(updateDto.getTitle())){
            needToUpdateItemCategory = true;
        }
        category = CategoryMapper.MAPPER.mapToPojo(category, updateDto);
        categoryRepository.save(category);
        if(needToUpdateItemCategory){
            updateCategoryDetailInItem(category);
        }
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

    public List<CategoryDto.DetailCategory> list(String data) {
        // Data >  Active | Inactive | Deleted | All
        List<Category> list = null;
        if (data.equals("Active")) {
            list = categoryRepository.findByActiveAndDeleted(true, false);
        } else if (data.equals("Inactive")) {
            list = categoryRepository.findByActiveAndDeleted(false, false);
        } else if (data.equals("Deleted")) {
            list = categoryRepository.findByDeleted(true);
        } else {
            list = categoryRepository.findAll();
        }
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

    public List<CategoryDto.DetailCategory> listDisplayCategory(String data) {
        // Data >  Active | Inactive | Deleted | All
        List<Category> list = null;
        if (data.equals("Active")) {
            list = categoryRepository.findByActiveAndDeletedForDisplay(true, false);
        } else if (data.equals("Inactive")) {
            list = categoryRepository.findByActiveAndDeletedForDisplay(false, false);
        } else if (data.equals("Deleted")) {
            list = categoryRepository.findByDeletedForDisplay(true);
        } else {
            list = categoryRepository.findByDisplayCategory();
        }
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
        category.setDeleted(true);
        category.setActive(false);
        category.setModifiedAt(LocalDateTime.now());
        categoryRepository.save(category);
    }

    @Override
    public void revive(String uuid) throws BadRequestException {
        Category category = findByUuid(uuid);
        category.setDeleted(false);
        category.setModifiedAt(LocalDateTime.now());
        categoryRepository.save(category);
    }

    public List<KeyValueDto> listInKeyValue() {
        List<Category> categoryList = categoryRepository.findByActiveAndDeleted();
        return categoryList.stream()
                .map(category -> CategoryMapper.MAPPER.mapToKeyPairDto(category))
                .collect(Collectors.toList());
    }

    public List<KeyValueDto> listInKeyValueForDisplayCategory() {
        List<Category> categoryList = categoryRepository.findByActiveAndDeletedAndDisplayCategory();
        return categoryList.stream()
                .map(category -> CategoryMapper.MAPPER.mapToKeyPairDto(category))
                .collect(Collectors.toList());
    }

    public void assignCategoryToItem(String uuid, List<String> itemUuids) throws BadRequestException {
        Category category = findByUuid(uuid);
        Category parentCategory = null;
        if (category.getParentCategoryId() != null) {
            parentCategory = findById(category.getParentCategoryId());
        }
        if (TextUtils.isEmpty(itemUuids)) {
            throw new BadRequestException("Invalid item assign, please check item Ids");
        }
        List<Item> itemList = itemRepository.findByUuids(itemUuids);
        if (TextUtils.isEmpty(itemList)) {
            throw new BadRequestException("Please provide valid item");
        }
        for (Item item : itemList) {
            if (parentCategory != null) {
                {
                    // update parent category
                    List<ObjectId> parentCategoryIds = item.getParentCategoryIds();
                    List<BasicParent> parentCategoryDetails = item.getParentCategoryDetails();
                    parentCategoryIds.add(parentCategory.getId());
                    parentCategoryDetails.add(new BasicParent(parentCategory.getUuid(), parentCategory.getTitle()));
                    item.setParentCategoryIds(parentCategoryIds);
                    item.setParentCategoryDetails(parentCategoryDetails);
                }
                {
                    // update sub category
                    List<ObjectId> subCategoryIds = item.getSubCategoryIds();
                    List<BasicParent> subCategoryDetails = item.getParentCategoryDetails();
                    subCategoryIds.add(category.getId());
                    subCategoryDetails.add(new BasicParent(category.getUuid(), category.getTitle()));
                    item.setSubCategoryIds(subCategoryIds);
                    item.setSubCategoryDetails(subCategoryDetails);
                }
            } else {
                List<ObjectId> parentCategoryIds = item.getParentCategoryIds();
                List<BasicParent> parentCategoryDetails = item.getParentCategoryDetails();
                parentCategoryIds.add(category.getId());
                parentCategoryDetails.add(new BasicParent(category.getUuid(), category.getTitle()));
                item.setParentCategoryIds(parentCategoryIds);
                item.setParentCategoryDetails(parentCategoryDetails);
            }
        }
        itemRepository.saveAll(itemList);
    }

    public List<KeyValueDto> parentCategoryListInKeyValue() {
        List<Category> categoryList = categoryRepository.getCategoryList(null);
        return categoryList.stream()
                .map(category -> CategoryMapper.MAPPER.mapToKeyPairDto(category))
                .collect(Collectors.toList());
    }

    public List<KeyValueDto> subCategoryListInKeyValue(String parentCategoryUuid) throws BadRequestException {
        Category parentCategory = findByUuid(parentCategoryUuid);
        List<Category> categoryList = categoryRepository.getCategoryList(parentCategory.getId());
        return categoryList.stream()
                .map(category -> CategoryMapper.MAPPER.mapToKeyPairDto(category))
                .collect(Collectors.toList());
    }

    public List<KeyValueDto> subCategoryListInKeyValue(List<String> parentCategoryUuids) throws BadRequestException {
        if (TextUtils.isEmpty(parentCategoryUuids)) {
            throw new BadRequestException("please provide parent category ids");
        }
        List<Category> parentCategoryList = categoryRepository.findByUuids(parentCategoryUuids);
        Set<ObjectId> parentCategoryIds = new TreeSet<>();
        parentCategoryList.stream().map(category -> parentCategoryIds.add(category.getId())).collect(Collectors.toList());
        List<Category> subCategoryList = categoryRepository.findByParentCategoryIds(parentCategoryIds.stream().toList());
        return subCategoryList.stream()
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


    public List<CategoryDto.CatSubCatSequenceDetail> categorySequenceDetail() {
        List<Category> list =categoryRepository.findParentCategories();
        return list.stream()
                .map(category -> CategoryMapper.MAPPER.mapToSequnceDetailDto(category))
                .collect(Collectors.toList());
    }

    public List<CategoryDto.CatSubCatSequenceDetail> displayCategorySequenceDetail() {
        List<Category> list =categoryRepository.findByActiveAndDeletedAndDisplayCategory();
        return list.stream()
                .map(category -> CategoryMapper.MAPPER.mapToSequnceDetailDto(category))
                .collect(Collectors.toList());
    }

    public List<CategoryDto.CatSubCatSequenceDetail> subCategorySequenceDetail(String parentCategoryUuid) throws BadRequestException {
        Category category = findByUuid(parentCategoryUuid);
        List<Category> list =categoryRepository.getCategoryList(category.getId());
        return list.stream()
                .map(category1 -> CategoryMapper.MAPPER.mapToSequnceDetailDto(category1))
                .collect(Collectors.toList());
    }

}