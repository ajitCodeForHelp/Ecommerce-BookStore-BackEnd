package com.bt.ecommerce.primary.service;

import com.bt.ecommerce.bean.DataTableResponsePacket;
import com.bt.ecommerce.bean.KeyValueDto;
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

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class AddressService extends _BaseService implements _BaseServiceImpl {
    @Override
    public String save(AbstractDto.Save saveDto) throws BadRequestException {
        Customer loggedInUser = (Customer) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        AddressDto.SaveAddress saveAddress = (AddressDto.SaveAddress) saveDto;
        Address address = AddressMapper.MAPPER.mapToSaveAddress(saveAddress);
        address.setCustomerId(loggedInUser.getId());

        BasicParent parent = new BasicParent();
        parent.setParentTitle(loggedInUser.fullName());
        parent.setParentUuid(loggedInUser.getUuid());
        address.setCustomerDetail(parent);

        addressRepository.save(address);
        return address.getUuid();
    }

    @Override
    public void update(String uuid, AbstractDto.Update updateDto) throws BadRequestException {
        AddressDto.UpdateAddress updateAddress = (AddressDto.UpdateAddress) updateDto;
        Address address = findByUuid(uuid);
        mapToUpdateAddress(address, updateAddress);
        addressRepository.save(address);
    }

    private void mapToUpdateAddress(Address address, AddressDto.UpdateAddress updateAddress) {
        address.setFirstName(updateAddress.getFirstName());
        address.setLastName(updateAddress.getLastName());
        address.setMobileNumber(updateAddress.getMobileNumber());
        address.setLatitude(updateAddress.getLatitude());
        address.setLongitude(updateAddress.getLongitude());
        address.setCountryTitle(updateAddress.getCountryTitle());
        address.setStateTitle(updateAddress.getStateTitle());
        address.setCityTitle(updateAddress.getCityTitle());
        address.setPinCode(updateAddress.getPinCode());
//        address.setAddressType(updateAddress.getAddressType());
    }

    @Override
    public AbstractDto.Detail get(String uuid) throws BadRequestException {
        Address address = findByUuid(uuid);
        return AddressMapper.MAPPER.mapToDetailAddress(address);
    }

    @Override
    public DataTableResponsePacket list(Boolean deleted, Integer pageNumber, Integer pageSize, String search) {
        return null;
    }

    @Override
    public void activate(String uuid) throws BadRequestException {

    }

    @Override
    public void inactivate(String uuid) throws BadRequestException {

    }

    @Override
    public void delete(String uuid) throws BadRequestException {
        Address address = findByUuid(uuid);
        address.setActive(false);
        address.setDeleted(true);
        address.setModifiedAt(LocalDateTime.now());
        addressRepository.save(address);
    }

    @Override
    public void revive(String uuid) throws BadRequestException {

    }

    @Override
    public List<KeyValueDto> listInKeyValue() throws BadRequestException {
        return null;
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

    private Address findByUuid(String uuid) throws BadRequestException {
        Address address = addressRepository.findByUuid(uuid);
        if (address == null) {
            throw new BadRequestException("ecommerce.common.message.record_not_exist");
        }
        return address;
    }
}
