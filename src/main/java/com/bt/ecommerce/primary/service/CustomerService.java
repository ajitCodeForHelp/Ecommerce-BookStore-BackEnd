package com.bt.ecommerce.primary.service;

import com.bt.ecommerce.bean.DataTableResponsePacket;
import com.bt.ecommerce.bean.KeyValueDto;
import com.bt.ecommerce.configuration.SpringBeanContext;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.dto.AbstractDto;
import com.bt.ecommerce.primary.dto.CustomerDto;
import com.bt.ecommerce.primary.dto.StaffDto;
import com.bt.ecommerce.primary.mapper.CustomerMapper;
import com.bt.ecommerce.primary.mapper.SystemUserMapper;
import com.bt.ecommerce.primary.pojo.common.BasicParent;
import com.bt.ecommerce.primary.pojo.enums.RoleEnum;
import com.bt.ecommerce.primary.pojo.user.Customer;
import com.bt.ecommerce.primary.pojo.user.SystemUser;
import com.bt.ecommerce.security.JwtUserDetailsService;
import com.bt.ecommerce.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
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
public class CustomerService extends _BaseService implements _BaseServiceImpl {
    @Override
    public String save(AbstractDto.Save saveDto) throws BadRequestException {
        CustomerDto.SaveCustomer saveCustomer = (CustomerDto.SaveCustomer) saveDto;
        Customer customer = CustomerMapper.MAPPER.mapToPojo(saveCustomer);
        if (checkUserExistWithMobile(saveCustomer.getIsdCode(), saveCustomer.getMobile())) {
            throw new BadRequestException("kpis_food.common.message.mobile_already_exist");
        }
        if (checkUserExistWithEmail(saveCustomer.getEmail())) {
            throw new BadRequestException("kpis_food.common.message.email_already_exist");
        }
        customer.setEmail(saveCustomer.getEmail());
        customer.setUsername(saveCustomer.getEmail());
        customer.setPwdText(saveCustomer.getPassword());
        customer.setPwdSecure(TextUtils.getEncodedPassword(saveCustomer.getPassword()));
        customer.setUserType(RoleEnum.ROLE_CUSTOMER);
        customer.setUniqueKey(TextUtils.getUniqueKey());
        customer = customerRepository.save(customer);
        return customer.getUuid();
    }

    public boolean checkUserExistWithMobile(String isdCode, String mobile) {
        return customerRepository.existsByIsdCodeAndMobile(isdCode, mobile);
    }

    public boolean checkUserExistWithEmail(String email) {
        return customerRepository.existsByEmail(email);
    }

    @Override
    public void update(String uuid, AbstractDto.Update updateDto) throws BadRequestException {
        CustomerDto.UpdateCustomer updateCustomerDto = (CustomerDto.UpdateCustomer) updateDto;
        Customer customer = findByUuid(uuid);
        customer = CustomerMapper.MAPPER.mapToPojo(customer, updateCustomerDto);
        {
            Customer userExistWithMobile = findFirstByIsdAndMobileAndId(updateCustomerDto.getIsdCode(), updateCustomerDto.getMobile(), customer.getId());
            if (userExistWithMobile != null) {
                throw new BadRequestException("kpis_food.common.message.mobile_already_exist");
            }
        }
        {
            Customer userExistWithEmail = findFirstByEmailAndId(updateCustomerDto.getEmail(), customer.getId());
            if (userExistWithEmail != null) {
                throw new BadRequestException("kpis_food.common.message.mobile_already_exist");
            }
        }
        customer.setEmail(updateCustomerDto.getEmail());
        customer.setUsername(updateCustomerDto.getEmail());
        customer.setUserType(RoleEnum.ROLE_CUSTOMER);
        customer.setUniqueKey(TextUtils.getUniqueKey());
        customerRepository.save(customer);
    }

    public Customer findFirstByIsdAndMobileAndId(String isdCode, String mobile, ObjectId id) {
        return customerRepository.findFirstByIsdCodeAndMobileAndId(isdCode, mobile, id);
    }

    public Customer findFirstByEmailAndId(String email, ObjectId id) {
        return customerRepository.findFirstByEmailAndId(email, id);
    }

    private Customer findByUuid(String uuid) throws BadRequestException {
        Customer customer = customerRepository.findByUuid(uuid);
        if (customer == null) {
            throw new BadRequestException("ecommerce.common.message.record_not_exist");
        }
        return customer;
    }

    @Override
    public AbstractDto.Detail get(String uuid) throws BadRequestException {
        Customer customer = findByUuid(uuid);
        return CustomerMapper.MAPPER.mapToDetailDto(customer);
    }

    @Override
    public DataTableResponsePacket list(Boolean deleted, Integer pageNumber, Integer pageSize, String search) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Customer> pageData = customerRepository.findByDeleted(deleted, search, pageable);
        return getDataTableResponsePacket(pageData, pageData.getContent().stream()
                .map(customer -> CustomerMapper.MAPPER.mapToDetailDto(customer))
                .collect(Collectors.toList()));
    }

    @Override
    public void activate(String uuid) throws BadRequestException {
        Customer customer = findByUuid(uuid);
        customer.setActive(true);
        customer.setModifiedAt(LocalDateTime.now());
        customerRepository.save(customer);
    }

    @Override
    public void inactivate(String uuid) throws BadRequestException {
        Customer customer = findByUuid(uuid);
        customer.setActive(false);
        customer.setModifiedAt(LocalDateTime.now());
        customerRepository.save(customer);
    }

    @Override
    public void delete(String uuid) throws BadRequestException {
        Customer customer = findByUuid(uuid);
        customer.setActive(false);
        customer.setDeleted(true);
        customer.setModifiedAt(LocalDateTime.now());
        customerRepository.save(customer);
    }

    @Override
    public void revive(String uuid) throws BadRequestException {

    }

    @Override
    public List<KeyValueDto> listInKeyValue() throws BadRequestException {
        return null;
    }
}
