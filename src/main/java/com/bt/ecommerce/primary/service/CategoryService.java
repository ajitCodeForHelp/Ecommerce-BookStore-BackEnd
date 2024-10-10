package com.bt.ecommerce.primary.service;

import com.bt.ecommerce.bean.DataTableResponsePacket;
import com.bt.ecommerce.bean.KeyValueDto;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.dto.AbstractDto;
import com.bt.ecommerce.primary.dto.CategoryDto;
import com.bt.ecommerce.primary.mapper.CategoryMapper;
import com.bt.ecommerce.primary.pojo.Category;
import com.bt.ecommerce.primary.pojo.common.BasicParent;
import com.bt.ecommerce.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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

    @Override
    public AbstractDto.Detail get(String uuid) throws BadRequestException {
        Category category = findByUuid(uuid);
        return CategoryMapper.MAPPER.mapToDetailDto(category);
    }

    @Override
    public DataTableResponsePacket list(Boolean deleted, Integer pageNumber, Integer pageSize, String search) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Category> pageData = categoryRepository.findByDeleted(deleted, search, pageable);
        return getDataTableResponsePacket(pageData, pageData.getContent().stream()
                .map(category -> CategoryMapper.MAPPER.mapToDetailDto(category))
                .collect(Collectors.toList()));
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

    private Category findByUuid(String uuid) throws BadRequestException {
        Category category = categoryRepository.findByUuid(uuid);
        if (category == null) {
            throw new BadRequestException("ecommerce.common.message.record_not_exist");
        }
        return category;
    }
}