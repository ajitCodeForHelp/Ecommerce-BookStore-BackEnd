package com.bt.ecommerce.primary.service;

import com.bt.ecommerce.bean.DataTableResponsePacket;
import com.bt.ecommerce.bean.KeyValueDto;
import com.bt.ecommerce.configuration.SpringBeanContext;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.dto.AbstractDto;
import com.bt.ecommerce.primary.dto.CategoryDto;
import com.bt.ecommerce.primary.dto.ItemDto;
import com.bt.ecommerce.primary.mapper.CategoryMapper;
import com.bt.ecommerce.primary.mapper.ItemMapper;
import com.bt.ecommerce.primary.pojo.Category;
import com.bt.ecommerce.primary.pojo.Item;
import com.bt.ecommerce.primary.pojo.Publisher;
import com.bt.ecommerce.primary.pojo.Tax;
import com.bt.ecommerce.primary.pojo.common.BasicParent;
import com.bt.ecommerce.primary.repository.SequenceRepository;
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
public class ItemService extends _BaseService implements _BaseServiceImpl {

    @Override
    public String save(AbstractDto.Save save) throws BadRequestException {
        ItemDto.SaveItem saveItemDto = (ItemDto.SaveItem) save;
        Item item = ItemMapper.MAPPER.mapToPojo(saveItemDto);
        item = updateItemCategory(item, saveItemDto.getParentCategoryUuids(), saveItemDto.getSubCategoryUuids() ,saveItemDto.getTaxUuid(),saveItemDto.getPublisherUuid());
        item = itemRepository.save(item);
        item.setItemCode("SKU-" +SpringBeanContext.getBean(SequenceRepository.class).getNextSequenceId(Item.class.getSimpleName()));
        itemRepository.save(item);
        return item.getUuid();
    }

    @Override
    public void update(String uuid, AbstractDto.Update update) throws BadRequestException {
        ItemDto.UpdateItem updateItemDto = (ItemDto.UpdateItem) update;
        Item item = findByUuid(uuid);
        item = ItemMapper.MAPPER.mapToPojo(item, updateItemDto);
        item = updateItemCategory(item, updateItemDto.getParentCategoryUuids(), updateItemDto.getSubCategoryUuids(),updateItemDto.getTaxUuid(),updateItemDto.getPublisherUuid());
        itemRepository.save(item);
    }

    private Item updateItemCategory(Item item, List<String> parentCategoryUuids, List<String> subCategoryUuids , String taxId , String publisherId) throws BadRequestException {
        if (!TextUtils.isEmpty(parentCategoryUuids)) {
            List<Category> categoryList = categoryRepository.findByUuids(parentCategoryUuids);
            if (categoryList == null || categoryList.isEmpty()) {
                throw new BadRequestException("ecommerce.common.message.record_not_exist");
            }
            item.setParentCategoryIds(categoryList.stream()
                    .map(category -> category.getId())
                    .collect(Collectors.toList()));
            item.setParentCategoryDetails(categoryList.stream()
                    .map(category -> new BasicParent(category.getUuid(), category.getTitle()))
                    .collect(Collectors.toList()));
        }
        if (!TextUtils.isEmpty(subCategoryUuids)) {
            List<Category> subCategoryList = categoryRepository.findByUuids(subCategoryUuids);
            if (subCategoryList == null || subCategoryList.isEmpty()) {
                throw new BadRequestException("ecommerce.common.message.record_not_exist");
            }
            item.setSubCategoryIds(subCategoryList.stream()
                    .map(category -> category.getId())
                    .collect(Collectors.toList()));
            item.setSubCategoryDetails(subCategoryList.stream()
                    .map(subCategory -> new BasicParent(subCategory.getUuid(), subCategory.getTitle()))
                    .collect(Collectors.toList()));
        }
        if (!TextUtils.isEmpty(subCategoryUuids)) {
            List<Category> subCategoryList = categoryRepository.findByUuids(subCategoryUuids);
            if (subCategoryList == null || subCategoryList.isEmpty()) {
                throw new BadRequestException("ecommerce.common.message.record_not_exist");
            }
            item.setSubCategoryIds(subCategoryList.stream()
                    .map(category -> category.getId())
                    .collect(Collectors.toList()));
            item.setSubCategoryDetails(subCategoryList.stream()
                    .map(subCategory -> new BasicParent(subCategory.getUuid(), subCategory.getTitle()))
                    .collect(Collectors.toList()));
        }
        if (!TextUtils.isEmpty(taxId)) {
           Tax tax = taxRepository.findByUuid(taxId);
            if (tax == null) {
                throw new BadRequestException("ecommerce.common.message.record_not_exist");
            }
            item.setTaxId(tax.getId());
            item.setTaxDetails(new BasicParent(tax.getUuid(), tax.getTitle()));
        }
        if (!TextUtils.isEmpty(publisherId)) {
            Publisher publisher = publisherRepository.findByUuid(publisherId);
            if (publisher == null) {
                throw new BadRequestException("ecommerce.common.message.record_not_exist");
            }
            item.setPublisherId(publisher.getId());
            item.setPublisherDetails(new BasicParent(publisher.getUuid(), publisher.getTitle()));
        }

        return item;
    }

    @Override
    public AbstractDto.Detail get(String uuid) throws BadRequestException {
        Item item = findByUuid(uuid);
        return ItemMapper.MAPPER.mapToDetailDto(item);
    }

//    private ItemDto.DetailItem mapToDetailDto(Item item) {
//        ItemDto.DetailItem detailItem = ItemMapper.MAPPER.mapToDetailDto(item);
//        if (!TextUtils.isEmpty(item.getParentCategoryIds())) {
//            List<Category> parentCategoryList = categoryRepository.findByIds(item.getParentCategoryIds());
//            if (parentCategoryList != null || !parentCategoryList.isEmpty()) {
//                detailItem.setParentCategoryDetail(parentCategoryList.stream()
//                        .map(category -> new BasicParent(category.getUuid(), category.getTitle()))
//                        .collect(Collectors.toList()));
//            }
//        }
//        if (!TextUtils.isEmpty(item.getSubCategoryIds())) {
//            List<Category> subCategoryList = categoryRepository.findByIds(item.getSubCategoryIds());
//            if (subCategoryList != null || !subCategoryList.isEmpty()) {
//                detailItem.setSubCategoryDetail(subCategoryList.stream()
//                        .map(category -> new BasicParent(category.getUuid(), category.getTitle()))
//                        .collect(Collectors.toList()));
//            }
//        }
//        return detailItem;
//    }

    @Override
    public DataTableResponsePacket list(Boolean deleted, Integer pageNumber, Integer pageSize, String search) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Item> pageData = itemRepository.findByDeleted(deleted, search, pageable);
        return getDataTableResponsePacket(pageData, pageData.getContent().stream()
                .map(item -> ItemMapper.MAPPER.mapToDetailDto(item))
                .collect(Collectors.toList()));
    }

    public List<ItemDto.DetailItem> listData(String data) {
        // Data >  Active | Inactive | Deleted | All
        List<Item> list = null;
        if (data.equals("Active")) {
            list = itemRepository.findByActiveAndDeleted(true, false);
        } else if (data.equals("Inactive")) {
            list = itemRepository.findByActiveAndDeleted(false, false);
        } else if (data.equals("Deleted")) {
            list = itemRepository.findByDeleted(true);
        } else {
            list = itemRepository.findAll();
        }
        return list.stream()
                .map(item -> ItemMapper.MAPPER.mapToDetailDto(item))
                .collect(Collectors.toList());
    }

    @Override
    public void activate(String uuid) throws BadRequestException {
        Item item = findByUuid(uuid);
        item.setActive(true);
        item.setModifiedAt(LocalDateTime.now());
        itemRepository.save(item);
    }

    @Override
    public void inactivate(String uuid) throws BadRequestException {
        Item item = findByUuid(uuid);
        item.setActive(false);
        item.setModifiedAt(LocalDateTime.now());
        itemRepository.save(item);
    }

    @Override
    public void delete(String uuid) throws BadRequestException {
        Item item = findByUuid(uuid);
        item.setDeleted(true);
        item.setActive(false);
        itemRepository.save(item);
    }

    @Override
    public void revive(String uuid) throws BadRequestException {
        Item item = findByUuid(uuid);
        item.setDeleted(false);
        item.setModifiedAt(LocalDateTime.now());
        itemRepository.save(item);
    }

    public void updateStockOut(String uuid) throws BadRequestException {
        Item item = findByUuid(uuid);
        item.setStockOut(!item.isStockOut());
        item.setModifiedAt(LocalDateTime.now());
        item = itemRepository.save(item);
        if (!item.isStockOut()) {
            // Item Available In Stock Then Send A Alert To Interested User
            SpringBeanContext.getBean(StockInNotificationService.class).notifyAllInterestedCustomer(item.getId());
        }
    }

    public List<KeyValueDto> listInKeyValue() {
        List<Item> itemList = itemRepository.findByActiveAndDeleted();
        return itemList.stream()
                .map(item -> ItemMapper.MAPPER.mapToKeyPairDto(item))
                .collect(Collectors.toList());
    }

    private Item findByUuid(String uuid) throws BadRequestException {
        Item item = itemRepository.findByUuid(uuid);
        if (item == null) {
            throw new BadRequestException("ecommerce.common.message.record_not_exist");
        }
        return item;
    }

    public List<ItemDto.ItemSearchDto> itemSearch(String search) {
        // Only Send 10 Record In Search Field
        Pageable pageable = PageRequest.of(0, 50);
        if (TextUtils.isEmpty(search)) return null;
        List<Item> itemList = itemRepository.findByTitle(search, pageable);
        if (TextUtils.isEmpty(itemList)) return null;
        return itemList.stream()
                .map(item -> ItemMapper.MAPPER.mapToItemSearchDto(item))
                .collect(Collectors.toList());
    }


    public ItemDto.ItemSearchDto itemDetail(String itemUuid) throws BadRequestException {
        Item item = findByUuid(itemUuid);
        return  ItemMapper.MAPPER.mapToItemSearchDto(item);
    }


    public List<ItemDto.ItemSequenceDetail> itemSequenceDetailByCategory(String categoryUuid) throws BadRequestException {
        Category category = categoryRepository.findByUuid(categoryUuid);
        if(category==null){
            throw new BadRequestException("ecommerce.common.message.record_not_exist");
        }
        List<Item> list =itemRepository.findByCategoryId(category.getId());
        return list.stream()
                .map(item -> ItemMapper.MAPPER.mapToSequnceDetailDto(item))
                .collect(Collectors.toList());
    }

    public void reorderingItemSequence(ItemDto.ItemSequenceReorder itemSequenceReorder) {
        for (ItemDto.ItemSequence sequence : itemSequenceReorder.getItemSequences()) {
            Item item = itemRepository.findByUuid(sequence.getId());
            item.setSequenceNo(sequence.getSequenceNo());
            itemRepository.save(item);
        }
    }

    public void updateItemCodesForExistingItems() {
        // Step 1: Retrieve all items sorted by createdAt in ascending order
        List<Item> items = itemRepository.findAll(Sort.by(Sort.Order.asc("createdAt")));

        // Step 2: Initialize the sequence number
//        int sequence = 1;

        // Step 3: Loop through each item and assign itemCode
        for (Item item : items) {
            // Generate the itemCode based on sequence (e.g., "1", "2", "3", ...)
//            String itemCode = "SKU-"+sequence;
            item.setItemCode("SKU-" +SpringBeanContext.getBean(SequenceRepository.class).getNextSequenceId(Item.class.getSimpleName()));

            // Step 4: Update the item with the generated itemCode
//            item.setItemCode(itemCode);

            // Save the updated item back to the repository
            itemRepository.save(item);

            // Increment the sequence for the next item
//            sequence++;
        }
    }

}