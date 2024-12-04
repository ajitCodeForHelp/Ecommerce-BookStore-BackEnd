package com.bt.ecommerce.primary.service;

import com.bt.ecommerce.bean.DataTableResponsePacket;
import com.bt.ecommerce.bean.KeyValueDto;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.dto.AbstractDto;
import com.bt.ecommerce.primary.dto.DynamicFieldDto;
import com.bt.ecommerce.primary.dto.TaxDto;
import com.bt.ecommerce.primary.mapper.DynamicFieldMapper;
import com.bt.ecommerce.primary.mapper.PublisherMapper;
import com.bt.ecommerce.primary.mapper.TaxMapper;
import com.bt.ecommerce.primary.pojo.DynamicField;
import com.bt.ecommerce.primary.pojo.Publisher;
import com.bt.ecommerce.primary.pojo.Tax;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TaxService extends _BaseService implements _BaseServiceImpl{
    @Override
    public String save(AbstractDto.Save saveDto) throws BadRequestException {
        TaxDto.SaveTax saveTax = (TaxDto.SaveTax) saveDto;
        Tax tax = TaxMapper.MAPPER.mapToPojo(saveTax);
        taxRepository.save(tax);
        return tax.getUuid();
    }

    @Override
    public void update(String uuid, AbstractDto.Update updateDto) throws BadRequestException {
        TaxDto.UpdateTax updateTax = (TaxDto.UpdateTax) updateDto;
        Tax tax = findByUuid(uuid);
        tax = TaxMapper.MAPPER.mapToPojo(tax, updateTax);
        taxRepository.save(tax);
    }



    private Tax findByUuid(String uuid) throws BadRequestException {
        Tax tax = taxRepository.findByUuid(uuid);
        if (tax == null) {
            throw new BadRequestException("ecommerce.common.message.record_not_exist");
        }
        return tax;
    }

    @Override
    public AbstractDto.Detail get(String uuid) throws BadRequestException {
        Tax tax = findByUuid(uuid);
        return TaxMapper.MAPPER.mapToTaxDetailDto(tax);
    }

    @Override
    public DataTableResponsePacket list(Boolean deleted, Integer pageNumber, Integer pageSize, String search) {
        return null;
    }

    @Override
    public void activate(String uuid) throws BadRequestException {
        Tax tax = findByUuid(uuid);
        tax.setActive(true);
        tax.setModifiedAt(LocalDateTime.now());
        taxRepository.save(tax);
    }

    @Override
    public void inactivate(String uuid) throws BadRequestException {
        Tax tax = findByUuid(uuid);
        tax.setActive(false);
        tax.setModifiedAt(LocalDateTime.now());
        taxRepository.save(tax);
    }

    @Override
    public void delete(String uuid) throws BadRequestException {
        Tax tax = findByUuid(uuid);
        tax.setActive(false);
        tax.setDeleted(true);
        taxRepository.save(tax);
    }

    @Override
    public void revive(String uuid) throws BadRequestException {
        Tax tax = findByUuid(uuid);
        tax.setDeleted(false);
        tax.setModifiedAt(LocalDateTime.now());
        taxRepository.save(tax);
    }

    @Override
    public List<KeyValueDto> listInKeyValue() throws BadRequestException {
        List<Tax> taxList = taxRepository.findByActiveAndDeleted(true,false);
        return taxList.stream()
                .map(tax -> TaxMapper.MAPPER.mapToKeyPairDto(tax))
                .collect(Collectors.toList());    }



    public List<TaxDto.DetailTax> taxList(String data) {
        // Data >  Active | Inactive | Deleted | All
        List<Tax> list = null;
        if (data.equals("Active")) {
            list = taxRepository.findByActiveAndDeleted(true, false);
        } else if (data.equals("Inactive")) {
            list = taxRepository.findByActiveAndDeleted(false, false);
        } else if (data.equals("Deleted")) {
            list = taxRepository.findByDeleted(true);
        } else {
            list = taxRepository.findAll();
        }
        return list.stream().map(TaxMapper.MAPPER::mapToTaxDetailDto).toList();
    }
}
