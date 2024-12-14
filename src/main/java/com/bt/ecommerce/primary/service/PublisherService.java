package com.bt.ecommerce.primary.service;

import com.bt.ecommerce.bean.DataTableResponsePacket;
import com.bt.ecommerce.bean.KeyValueDto;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.dto.AbstractDto;
import com.bt.ecommerce.primary.dto.PublisherDto;
import com.bt.ecommerce.primary.mapper.PublisherMapper;
import com.bt.ecommerce.primary.pojo.Item;
import com.bt.ecommerce.primary.pojo.Publisher;
import com.bt.ecommerce.primary.pojo.common.BasicParent;
import com.bt.ecommerce.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PublisherService extends _BaseService implements _BaseServiceImpl{
    @Override
    public String save(AbstractDto.Save saveDto) throws BadRequestException {
        PublisherDto.SavePublisher savePublisher = (PublisherDto.SavePublisher) saveDto;
        Publisher publisher = PublisherMapper.MAPPER.mapToPojo(savePublisher);
        publisherRepository.save(publisher);
        return publisher.getUuid();
    }

    @Override
    public void update(String uuid, AbstractDto.Update updateDto) throws BadRequestException {
        PublisherDto.UpdatePublisher updatePublisher = (PublisherDto.UpdatePublisher) updateDto;
        Publisher publisher = findByUuid(uuid);
        publisher =  PublisherMapper.MAPPER.mapToPojo(publisher , updatePublisher);
        publisherRepository.save(publisher);
        updatePublisherDetailInItem(publisher);
    }

    private Publisher findByUuid(String uuid) throws BadRequestException {
        Publisher publisher = publisherRepository.findByUuid(uuid);
        if (publisher == null) {
            throw new BadRequestException("ecommerce.common.message.record_not_exist");
        }
        return publisher;
    }

    @Override
    public AbstractDto.Detail get(String uuid) throws BadRequestException {
        Publisher publisher = findByUuid(uuid);
        return PublisherMapper.MAPPER.mapToPublisherDetailDto(publisher);
    }

    @Override
    public DataTableResponsePacket list(Boolean deleted, Integer pageNumber, Integer pageSize, String search) {
        return null;
    }

    @Override
    public void activate(String uuid) throws BadRequestException {
        Publisher publisher = findByUuid(uuid);
        publisher.setActive(true);
        publisher.setModifiedAt(LocalDateTime.now());
        publisherRepository.save(publisher);
    }

    @Override
    public void inactivate(String uuid) throws BadRequestException {
        Publisher publisher = findByUuid(uuid);
        publisher.setActive(false);
        publisher.setModifiedAt(LocalDateTime.now());
        publisherRepository.save(publisher);
    }

    @Override
    public void delete(String uuid) throws BadRequestException {
        Publisher publisher = findByUuid(uuid);
        publisher.setActive(false);
        publisher.setDeleted(true);
        publisherRepository.save(publisher);
    }

    @Override
    public void revive(String uuid) throws BadRequestException {
        Publisher publisher = findByUuid(uuid);
        publisher.setDeleted(false);
        publisher.setModifiedAt(LocalDateTime.now());
        publisherRepository.save(publisher);
    }

    @Override
    public List<KeyValueDto> listInKeyValue() throws BadRequestException {
        List<Publisher> publisherList = publisherRepository.findByActiveAndDeleted(true,false);
        return publisherList.stream()
                .map(publisher -> PublisherMapper.MAPPER.mapToKeyPairDto(publisher))
                .collect(Collectors.toList());
    }


    public List<PublisherDto.DetailPublisher> publisherList(String data) {
        // Data >  Active | Inactive | Deleted | All
        List<Publisher> list = null;
        if (data.equals("Active")) {
            list = publisherRepository.findByActiveAndDeleted(true, false);
        } else if (data.equals("Inactive")) {
            list = publisherRepository.findByActiveAndDeleted(false, false);
        } else if (data.equals("Deleted")) {
            list = publisherRepository.findByDeleted(true);
        } else {
            list = publisherRepository.findAll();
        }
        return list.stream().map(PublisherMapper.MAPPER::mapToPublisherDetailDto).toList();
    }

    private void updatePublisherDetailInItem(Publisher publisher){
        List<Item> itemList = itemRepository.findByPublisherId(publisher.getId());
        if(TextUtils.isEmpty(itemList)) return;
        for (Item item : itemList) {
            item.setPublisherDetails(new BasicParent(publisher.getUuid(), publisher.getTitle()));
        }
        itemRepository.saveAll(itemList);
    }
}
