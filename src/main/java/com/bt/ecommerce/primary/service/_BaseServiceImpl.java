package com.bt.ecommerce.primary.service;

import com.bt.ecommerce.bean.DataTableResponsePacket;
import com.bt.ecommerce.bean.KeyValueDto;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.dto.AbstractDto;

import java.util.List;

public interface _BaseServiceImpl {
    String save(AbstractDto.Save saveDto) throws BadRequestException;

    void update(String uuid, AbstractDto.Update updateDto) throws BadRequestException;

    AbstractDto.Detail get(String uuid) throws BadRequestException;

    DataTableResponsePacket list(Boolean deleted, Integer pageNumber, Integer pageSize, String search);

    void activate(String uuid) throws BadRequestException;

    void inactivate(String uuid) throws BadRequestException;

    void delete(String uuid) throws BadRequestException;

    void revive(String uuid) throws BadRequestException;

    List<KeyValueDto> listInKeyValue() throws BadRequestException;
}
