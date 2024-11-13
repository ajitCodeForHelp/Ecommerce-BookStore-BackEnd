package com.bt.ecommerce.primary.mapper;

import com.bt.ecommerce.primary.dto.AbstractDto;
import com.bt.ecommerce.primary.dto.CustomerDto;
import com.bt.ecommerce.primary.dto.StaffDto;
import com.bt.ecommerce.primary.pojo.user.Customer;
import com.bt.ecommerce.primary.pojo.user.SystemUser;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerMapper MAPPER = Mappers.getMapper(CustomerMapper.class);

    Customer mapToPojo(@MappingTarget Customer customer, CustomerDto.UpdateCustomer update);

    CustomerDto.DetailCustomer mapToDetailDto(Customer customer);
}
