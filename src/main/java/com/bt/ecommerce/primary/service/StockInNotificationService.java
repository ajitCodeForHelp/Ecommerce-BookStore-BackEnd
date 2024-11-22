package com.bt.ecommerce.primary.service;

import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.dto.StockInNotificationDto;
import com.bt.ecommerce.primary.mapper.StockInNotificationMapper;
import com.bt.ecommerce.primary.pojo.Item;
import com.bt.ecommerce.primary.pojo.StockInNotification;
import com.bt.ecommerce.primary.pojo.common.BasicParent;
import com.bt.ecommerce.primary.pojo.user.Customer;
import com.bt.ecommerce.primary.pojo.user.SystemUser;
import com.bt.ecommerce.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StockInNotificationService extends _BaseService{

    public void saveItemNotify(StockInNotificationDto.SaveItemNotification saveItemNotification) throws BadRequestException {
        StockInNotification stockInNotification = new StockInNotification();
        Item item = itemRepository.findByUuid(saveItemNotification.getItemUuid());
        if (item == null) {
            throw new BadRequestException("Invalid Item Provided.");
        }
        Customer customer = customerRepository.findFirstByIsdCodeAndMobile(saveItemNotification.getCustomerIsdCode(), saveItemNotification.getCustomerMobile());
        stockInNotification.setItemId(item.getId());
        stockInNotification.setItemDetails(new BasicParent(item.getUuid(), item.getTitle()));
        stockInNotification.setCustomerIsdCode(saveItemNotification.getCustomerIsdCode());
        stockInNotification.setCustomerMobile(saveItemNotification.getCustomerMobile());
        if (customer != null) {
            stockInNotification.setCustomerId(customer.getId());
        }
        stockInNotificationRepository.save(stockInNotification);
    }


    public void notifyAllInterestedCustomer(ObjectId itemId){
        List<StockInNotification> list = stockInNotificationRepository.findByItemIdAndDeleted(itemId, false);
        if(TextUtils.isEmpty(list)) return;
        // TODO >> Validate ANd Send Notification To ALl customer
        // TODO >> Messaging Service

        // Mark All As Deleted
        for (StockInNotification stockInNotification : list) {
            stockInNotification.setDeleted(false);
            stockInNotification.setDeleted(true);
        }
        stockInNotificationRepository.saveAll(list);
    }


    public List<StockInNotificationDto.DetailCustomerItemNotification> listData(String data) {
        // Data >  Active | Inactive | Deleted | All
        List<StockInNotification> list = null;
        if (data.equals("Active")) {
            list = stockInNotificationRepository.findByActiveAndNotified(true, false);
        } else if (data.equals("Notified")) {
            list = stockInNotificationRepository.findByActiveAndNotified(true, true);
        } else if (data.equals("Deleted")) {
            list = stockInNotificationRepository.findByDeleted(true);
        } else {
            list = stockInNotificationRepository.findAll();
        }
        return list.stream()
                .map(item -> StockInNotificationMapper.MAPPER.mapToDetailDto(item))
                .collect(Collectors.toList());
    }


    public void delete(String uuid) throws BadRequestException {
        StockInNotification stockInNotification = findByUuid(uuid);
        stockInNotification.setDeleted(true);
        stockInNotification.setActive(false);
        stockInNotificationRepository.save(stockInNotification);
    }


    public void notifyToCustomer(String uuid) throws BadRequestException {
        StockInNotification stockInNotification = findByUuid(uuid);
        stockInNotification.setNotifiedToCustomer(true);
        stockInNotificationRepository.save(stockInNotification);
    }

    private StockInNotification findByUuid(String uuid) throws BadRequestException {
        StockInNotification stockInNotification = stockInNotificationRepository.findByUuid(uuid);
        if (stockInNotificationRepository == null) {
            throw new BadRequestException("ecommerce.common.message.record_not_exist");
        }
        return stockInNotification;
    }

}
