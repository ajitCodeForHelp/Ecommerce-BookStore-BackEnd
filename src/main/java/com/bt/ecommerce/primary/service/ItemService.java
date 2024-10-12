package com.bt.ecommerce.primary.service;

import com.bt.ecommerce.bean.DataTableResponsePacket;
import com.bt.ecommerce.bean.KeyValueDto;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.dto.AbstractDto;
import com.bt.ecommerce.primary.dto.ItemDto;
import com.bt.ecommerce.primary.mapper.ItemMapper;
import com.bt.ecommerce.primary.pojo.Item;
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
//        Category category = null;
//        Category subCategory = null;
//        if (!TextUtils.isEmpty(saveItemDto.getCategoryUuid())) {
//            category = categoryRepository.findByUuid(saveItemDto.getCategoryUuid());
//            if (category == null) {
//                throw new BadRequestException("ecommerce.common.message.record_not_exist");
//            }
//        }
//        if (!TextUtils.isEmpty(saveItemDto.getSubCategoryUuid())) {
//            subCategory = categoryRepository.findByUuid(saveItemDto.getCategoryUuid());
//            if (subCategory == null) {
//                throw new BadRequestException("ecommerce.common.message.record_not_exist");
//            }
//        }
        Item item = ItemMapper.MAPPER.mapToPojo(saveItemDto);
        item = itemRepository.save(item);
        return item.getUuid();
    }

    @Override
    public void update(String uuid, AbstractDto.Update update) throws BadRequestException {
        ItemDto.UpdateItem updateItemDto = (ItemDto.UpdateItem) update;
        Item item = findByUuid(uuid);
        item = ItemMapper.MAPPER.mapToPojo(item, updateItemDto);
        itemRepository.save(item);
    }

    @Override
    public AbstractDto.Detail get(String uuid) throws BadRequestException {
        Item item = findByUuid(uuid);
        return ItemMapper.MAPPER.mapToDetailDto(item);
    }

    @Override
    public DataTableResponsePacket list(Boolean deleted, Integer pageNumber, Integer pageSize, String search) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Item> pageData = itemRepository.findByDeleted(deleted, search, pageable);
        return getDataTableResponsePacket(pageData, pageData.getContent().stream()
                .map(item -> ItemMapper.MAPPER.mapToDetailDto(item))
                .collect(Collectors.toList()));
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
        item.setModifiedAt(LocalDateTime.now());
        itemRepository.save(item);
    }

    @Override
    public void revive(String uuid) throws BadRequestException {
        Item item = findByUuid(uuid);
        item.setDeleted(false);
        item.setModifiedAt(LocalDateTime.now());
        itemRepository.save(item);
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
}