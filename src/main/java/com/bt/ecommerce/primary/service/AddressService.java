package com.bt.ecommerce.primary.service;

import com.bt.ecommerce.bean.DataTableResponsePacket;
import com.bt.ecommerce.bean.KeyValueDto;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.dto.AbstractDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AddressService extends _BaseService implements _BaseServiceImpl{
    @Override
    public String save(AbstractDto.Save saveDto) throws BadRequestException {
        return null;
    }

    @Override
    public void update(String uuid, AbstractDto.Update updateDto) throws BadRequestException {

    }

    @Override
    public AbstractDto.Detail get(String uuid) throws BadRequestException {
        return null;
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

    }

    @Override
    public void revive(String uuid) throws BadRequestException {

    }

    @Override
    public List<KeyValueDto> listInKeyValue() throws BadRequestException {
        return null;
    }
}
