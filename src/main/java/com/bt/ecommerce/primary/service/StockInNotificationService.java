package com.bt.ecommerce.primary.service;

import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.messaging.SmsComponent;
import com.bt.ecommerce.primary.dto.CustomerDto;
import com.bt.ecommerce.primary.dto.StockInNotificationDto;
import com.bt.ecommerce.primary.mapper.CustomerMapper;
import com.bt.ecommerce.primary.mapper.StockInNotificationMapper;
import com.bt.ecommerce.primary.pojo.DynamicField;
import com.bt.ecommerce.primary.pojo.Item;
import com.bt.ecommerce.primary.pojo.StockInNotification;
import com.bt.ecommerce.primary.pojo.common.BasicParent;
import com.bt.ecommerce.primary.pojo.user.Customer;
import com.bt.ecommerce.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StockInNotificationService extends _BaseService{

    @Autowired
    SmsComponent smsComponent;

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
        List<StockInNotification> list = stockInNotificationRepository.findByItemIdAndDeletedAndNotified(itemId, false, false);
        if(TextUtils.isEmpty(list)) return;
        // TODO >> Validate ANd Send Notification To ALl customer
        // TODO >> Messaging Service
        // Mark All As Deleted
        for (StockInNotification stockInNotification : list) {
            String notifyItemMessage = "Hello! Good news — " + stockInNotification.getItemDetails().getParentTitle() +
                    " is back in stock at The Books 24!" +
                    "Tap the link to order now: " +  "thebooks24.com"+
                    "  – Team The Books 24";
            smsComponent.sendSMSByMakeMySms(stockInNotification.getCustomerMobile(),notifyItemMessage,"1707174845303156700");
            stockInNotification.setNotified(true);
        }
        stockInNotificationRepository.saveAll(list);
    }

    public void delete(String uuid) throws BadRequestException {
        StockInNotification stockInNotification = stockInNotificationRepository.findByUuid(uuid);
        stockInNotification.setActive(false);
        stockInNotification.setDeleted(true);
        stockInNotificationRepository.save(stockInNotification);
    }

    public List<StockInNotificationDto.DetailItemNotification> listData(String data) {
        // Data >  Active | Inactive | Deleted | All
        List<StockInNotification> list = null;
        if (data.equals("Active")) {
            list = stockInNotificationRepository.findByActiveAndDeleted(true, false);
        } else if (data.equals("Inactive")) {
            list = stockInNotificationRepository.findByActiveAndDeleted(false, false);
        } else if (data.equals("Deleted")) {
            list = stockInNotificationRepository.findByDeleted(true);
        }
        else if (data.equals("Notified")) {
            list = stockInNotificationRepository.findByNotified(true);
        }
        else {
            list = stockInNotificationRepository.findAll();
        }
        return list.stream()
                .map(customer -> StockInNotificationMapper.MAPPER.mapToDetailDto(customer))
                .collect(Collectors.toList());
    }
}