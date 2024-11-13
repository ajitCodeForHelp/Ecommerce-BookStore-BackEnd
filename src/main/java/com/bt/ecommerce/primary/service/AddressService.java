package com.bt.ecommerce.primary.service;

import com.bt.ecommerce.configuration.SpringBeanContext;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.dto.AbstractDto;
import com.bt.ecommerce.primary.dto.AddressDto;
import com.bt.ecommerce.primary.mapper.AddressMapper;
import com.bt.ecommerce.primary.pojo.Address;
import com.bt.ecommerce.primary.pojo.common.BasicParent;
import com.bt.ecommerce.primary.pojo.user.Customer;
import com.bt.ecommerce.security.JwtUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AddressService extends _BaseService  {

    public String save(AddressDto.SaveAddress saveAddress) throws BadRequestException {
        Customer loggedInUser = (Customer) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        Address address = AddressMapper.MAPPER.mapToPojo(saveAddress);
        address.setCustomerId(loggedInUser.getId());
        address.setCustomerDetail(new BasicParent(loggedInUser.fullName(), loggedInUser.getUuid()));
        addressRepository.save(address);
        return address.getUuid();
    }


    public void update(String uuid, AddressDto.UpdateAddress updateDto) throws BadRequestException {
        Address address = findByUuid(uuid);
        AddressMapper.MAPPER.mapToPojo(address, updateDto);
        addressRepository.save(address);
    }

    public AbstractDto.Detail get(String uuid) throws BadRequestException {
        Address address = findByUuid(uuid);
        return AddressMapper.MAPPER.mapToDetailAddress(address);
    }

    public List<AddressDto.DetailAddress> listAllCustomerAddress(String data) {
        Customer loggedInUser = (Customer) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        // Data >  Active | Inactive | Deleted | All
        List<Address> list = null;
        if (data.equals("Active")) {
            list = addressRepository.findByCustomerIdAndActiveAndDeleted(loggedInUser.getId(), true, false);
        } else if (data.equals("Inactive")) {
            list = addressRepository.findByCustomerIdAndActiveAndDeleted(loggedInUser.getId(), false, false);
        } else if (data.equals("Deleted")) {
            list = addressRepository.findByCustomerIdAndDeleted(loggedInUser.getId(), true);
        } else {
            list = addressRepository.findByCustomerId(loggedInUser.getId());
        }
        return list.stream()
                .map(AddressMapper.MAPPER::mapToDetailAddress).toList();
    }

    public void delete(String uuid) throws BadRequestException {
        Address address = findByUuid(uuid);
        addressRepository.delete(address);
    }

    private Address findByUuid(String uuid) throws BadRequestException {
        Address address = addressRepository.findByUuid(uuid);
        if (address == null) {
            throw new BadRequestException("ecommerce.common.message.record_not_exist");
        }
        return address;
    }
}
