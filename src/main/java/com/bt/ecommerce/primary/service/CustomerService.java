package com.bt.ecommerce.primary.service;

import com.bt.ecommerce.bean.DataTableResponsePacket;
import com.bt.ecommerce.bean.KeyValueDto;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.dto.AbstractDto;
import com.bt.ecommerce.primary.dto.CustomerDto;
import com.bt.ecommerce.primary.mapper.CustomerMapper;
import com.bt.ecommerce.primary.pojo.enums.RoleEnum;
import com.bt.ecommerce.primary.pojo.user.Customer;
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

    public void generateOtp(CustomerDto.GenerateOtp generateOtp) {
        Customer customer = saveOrfindCustomer(generateOtp.getIsdCode(), generateOtp.getMobile());
        // Todo:> Generate an Otp and send it to the customer
    }

    private Customer saveOrfindCustomer(String isdCode, String mobile) {
        Customer customer = customerRepository.findFirstByIsdCodeAndMobile(isdCode, mobile);
        if (customer == null) {
            customer = new Customer();
            customer.setIsdCode(isdCode);
            customer.setMobile(mobile);
            customer.setUserType(RoleEnum.ROLE_CUSTOMER);
            customer.setUniqueKey(TextUtils.getUniqueKey());
            customerRepository.save(customer);
        }
        return customer;
    }

    public CustomerDto.DetailCustomer loginWithOtp(CustomerDto.LoginCustomer loginCustomer) throws BadRequestException {
        Customer customer = findUserWithMobile(loginCustomer.getIsdCode(), loginCustomer.getMobile());
        if (customer == null) {
            throw new BadRequestException("ecommerce.common.message.record_not_exist");
        }
        // Todo:> Verify the otp
        if (!loginCustomer.getPassword().isEmpty()) {
            customer.setPwdText(loginCustomer.getPassword());
            customer.setPwdSecure(TextUtils.getEncodedPassword(loginCustomer.getPassword()));
        }

        customer = customerRepository.save(customer);
        // Todo:> Generate auth for customer login
        return CustomerMapper.MAPPER.mapToDetailDto(customer);
    }

    public CustomerDto.DetailCustomer loginWithPassword(CustomerDto.LoginCustomer loginCustomer) throws BadRequestException {
        Customer customer = findUserWithMobile(loginCustomer.getIsdCode(), loginCustomer.getMobile());
        if (customer == null) {
            throw new BadRequestException("ecommerce.common.message.record_not_exist");
        }
        if (customer.getPwdSecure().isEmpty()) {
            throw new BadRequestException("ecommerce.common.message.password_not_set");
        }
        TextUtils.matchPassword(loginCustomer.getPassword(), customer.getPwdSecure());
        // Todo:> Generate auth for customer login
        return CustomerMapper.MAPPER.mapToDetailDto(customer);
    }

    public Customer findUserWithMobile(String isdCode, String mobile) {
        return customerRepository.findFirstByIsdCodeAndMobile(isdCode, mobile);
    }

    public boolean checkUserExistWithEmail(String email) {
        return customerRepository.existsByEmail(email);
    }

    @Override
    public String save(AbstractDto.Save saveDto) throws BadRequestException {
        return null;
    }

    @Override
    public void update(String uuid, AbstractDto.Update updateDto) throws BadRequestException {
        CustomerDto.UpdateCustomer updateCustomerDto = (CustomerDto.UpdateCustomer) updateDto;
        Customer customer = findByUuid(uuid);
        customer = CustomerMapper.MAPPER.mapToPojo(customer, updateCustomerDto);
        customer.setEmail(updateCustomerDto.getEmail());
        customer.setUsername(updateCustomerDto.getEmail());
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
                .map(CustomerMapper.MAPPER::mapToDetailDto)
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

    public List<CustomerDto.DetailCustomer> listData(String data) {
        // Data >  Active | Inactive | Deleted | All
        List<Customer> list = null;
        if (data.equals("Active")) {
            list = customerRepository.findByActiveAndDeleted(true, false);
        } else if (data.equals("Inactive")) {
            list = customerRepository.findByActiveAndDeleted(false, false);
        } else if (data.equals("Deleted")) {
            list = customerRepository.findByDeleted(true);
        } else {
            list = customerRepository.findAll();
        }
        return list.stream()
                .map(customer -> CustomerMapper.MAPPER.mapToDetailDto(customer))
                .collect(Collectors.toList());
    }

}
