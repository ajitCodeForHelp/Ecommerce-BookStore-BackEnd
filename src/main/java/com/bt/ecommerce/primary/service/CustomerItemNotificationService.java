package com.bt.ecommerce.primary.service;

import com.bt.ecommerce.bean.DataTableResponsePacket;
import com.bt.ecommerce.bean.KeyValueDto;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.dto.AbstractDto;
import com.bt.ecommerce.primary.dto.CustomerItemNotificationDto;
import com.bt.ecommerce.primary.dto.DynamicFieldDto;
import com.bt.ecommerce.primary.mapper.CustomerItemNotificationMapper;
import com.bt.ecommerce.primary.mapper.DynamicFieldMapper;
import com.bt.ecommerce.primary.pojo.CustomerItemNotification;
import com.bt.ecommerce.primary.pojo.DynamicField;
import com.bt.ecommerce.primary.pojo.user.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class CustomerItemNotificationService extends _BaseService{

    public String save(CustomerItemNotificationDto.SaveCustomerItemNotification create) {
        CustomerItemNotification customerItemNotification = CustomerItemNotificationMapper.MAPPER.mapToPojo(create);
        customerItemNotificationRepository.save(customerItemNotification);
        return customerItemNotification.getUuid();
    }

    public String save(Customer loggedInUser, CustomerItemNotificationDto.SaveCustomerItemNotification create) {
        CustomerItemNotification customerItemNotification = new CustomerItemNotification();
        customerItemNotification.setItemId(create.getItemId());
        customerItemNotification.setCustomerIsdCode(loggedInUser.getIsdCode());
        customerItemNotification.setCustomerMobile(loggedInUser.getMobile());
        customerItemNotificationRepository.save(customerItemNotification);
        return customerItemNotification.getUuid();
    }

    public List<CustomerItemNotificationDto.DetailCustomerItemNotification> customerItemNotification(String data) {
        List<CustomerItemNotification> list = customerItemNotificationRepository.findAll();
        // todo:> map the item details if needed
        return list.stream().map(CustomerItemNotificationMapper.MAPPER::mapToDetailDto).toList();
    }


}
