package com.bt.ecommerce.primary.service;

import com.bt.ecommerce.bean.DataTableResponsePacket;
import com.bt.ecommerce.bean.KeyValueDto;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.dto.AbstractDto;
import com.bt.ecommerce.primary.dto.DynamicFieldDto;
import com.bt.ecommerce.primary.mapper.DynamicFieldMapper;
import com.bt.ecommerce.primary.pojo.DynamicField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class DynamicFieldService extends _BaseService implements _BaseServiceImpl{
    @Override
    public String save(AbstractDto.Save saveDto) throws BadRequestException {
        DynamicFieldDto.SaveDynamicField saveDynamicField = (DynamicFieldDto.SaveDynamicField) saveDto;
        DynamicField dynamicField = DynamicFieldMapper.MAPPER.mapToPojo(saveDynamicField);
        dynamicFieldRepository.save(dynamicField);
        return dynamicField.getUuid();
    }

    @Override
    public void update(String uuid, AbstractDto.Update updateDto) throws BadRequestException {
        DynamicFieldDto.UpdateDynamicField updateDynamicField = (DynamicFieldDto.UpdateDynamicField) updateDto;
        DynamicField dynamicField = findByUuid(uuid);
        mapToUpdateDynamicField(dynamicField, updateDynamicField);
        dynamicFieldRepository.save(dynamicField);
    }

    private void mapToUpdateDynamicField(DynamicField dynamicField, DynamicFieldDto.UpdateDynamicField updateDynamicField) {
        dynamicField.setTitle(updateDynamicField.getTitle());
        dynamicField.setType(updateDynamicField.getType());
    }

    private DynamicField findByUuid(String uuid) throws BadRequestException {
        DynamicField dynamicField = dynamicFieldRepository.findByUuid(uuid);
        if (dynamicField == null) {
            throw new BadRequestException("ecommerce.common.message.record_not_exist");
        }
        return dynamicField;
    }

    @Override
    public AbstractDto.Detail get(String uuid) throws BadRequestException {
        DynamicField dynamicField = findByUuid(uuid);
        return DynamicFieldMapper.MAPPER.mapToCouponCodeDetailDto(dynamicField);
    }

    @Override
    public DataTableResponsePacket list(Boolean deleted, Integer pageNumber, Integer pageSize, String search) {
        return null;
    }

    @Override
    public void activate(String uuid) throws BadRequestException {
        DynamicField dynamicField = findByUuid(uuid);
        dynamicField.setActive(true);
        dynamicField.setModifiedAt(LocalDateTime.now());
        dynamicFieldRepository.save(dynamicField);
    }

    @Override
    public void inactivate(String uuid) throws BadRequestException {
        DynamicField dynamicField = findByUuid(uuid);
        dynamicField.setActive(false);
        dynamicField.setModifiedAt(LocalDateTime.now());
        dynamicFieldRepository.save(dynamicField);
    }

    @Override
    public void delete(String uuid) throws BadRequestException {
        DynamicField dynamicField = findByUuid(uuid);
        dynamicField.setActive(false);
        dynamicField.setDeleted(true);
        dynamicFieldRepository.save(dynamicField);
    }

    @Override
    public void revive(String uuid) throws BadRequestException {
        DynamicField dynamicField = findByUuid(uuid);
        dynamicField.setDeleted(false);
        dynamicField.setModifiedAt(LocalDateTime.now());
        dynamicFieldRepository.save(dynamicField);
    }

    @Override
    public List<KeyValueDto> listInKeyValue() throws BadRequestException {
        return null;
    }


    public List<DynamicFieldDto.DetailDynamicField> dynamicFieldList(String data) {
        // Data >  Active | Inactive | Deleted | All
        List<DynamicField> list = null;
        if (data.equals("Active")) {
            list = dynamicFieldRepository.findByActiveAndDeleted(true, false);
        } else if (data.equals("Inactive")) {
            list = dynamicFieldRepository.findByActiveAndDeleted(false, false);
        } else if (data.equals("Deleted")) {
            list = dynamicFieldRepository.findByDeleted(true);
        } else {
            list = dynamicFieldRepository.findAll();
        }
        return list.stream().map(DynamicFieldMapper.MAPPER::mapToCouponCodeDetailDto).toList();
    }
}
