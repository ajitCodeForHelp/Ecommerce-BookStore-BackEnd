package com.bt.ecommerce.primary.service;

import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.dto.StockInNotificationDto;
import com.bt.ecommerce.primary.pojo.Item;
import com.bt.ecommerce.primary.pojo.StockInNotification;
import com.bt.ecommerce.primary.pojo.common.BasicParent;
import com.bt.ecommerce.primary.pojo.user.Customer;
import com.bt.ecommerce.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;

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

}
