package com.bt.ecommerce.primary.service;

import com.bt.ecommerce.bean.DataTableResponsePacket;
import com.bt.ecommerce.bean.KeyValueDto;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.dto.AbstractDto;
import com.bt.ecommerce.primary.dto.CourierPartnerDto;
import com.bt.ecommerce.primary.mapper.CourierPartnerMapper;
import com.bt.ecommerce.primary.pojo.CourierPartner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CourierPartnerService extends _BaseService implements _BaseServiceImpl{
    @Override
    public String save(AbstractDto.Save saveDto) throws BadRequestException {
        CourierPartnerDto.SaveCourierPartner saveCourierPartner = (CourierPartnerDto.SaveCourierPartner) saveDto;
        CourierPartner courierPartner = CourierPartnerMapper.MAPPER.mapToPojo(saveCourierPartner);
        courierPartnerRepository.save(courierPartner);
        return courierPartner.getUuid();
    }

    @Override
    public void update(String uuid, AbstractDto.Update updateDto) throws BadRequestException {
        CourierPartnerDto.UpdateCourierPartner updateCourierPartner = (CourierPartnerDto.UpdateCourierPartner) updateDto;
        CourierPartner courierPartner = findByUuid(uuid);
        courierPartner =  CourierPartnerMapper.MAPPER.mapToPojo(courierPartner , updateCourierPartner);
        courierPartnerRepository.save(courierPartner);
    }

    private CourierPartner findByUuid(String uuid) throws BadRequestException {
        CourierPartner courierPartner = courierPartnerRepository.findByUuid(uuid);
        if (courierPartner == null) {
            throw new BadRequestException("ecommerce.common.message.record_not_exist");
        }
        return courierPartner;
    }

    @Override
    public AbstractDto.Detail get(String uuid) throws BadRequestException {
        CourierPartner courierPartner = findByUuid(uuid);
        return CourierPartnerMapper.MAPPER.mapToCourierPartnerDetailDto(courierPartner);
    }

    @Override
    public DataTableResponsePacket list(Boolean deleted, Integer pageNumber, Integer pageSize, String search) {
        return null;
    }

    @Override
    public void activate(String uuid) throws BadRequestException {
        CourierPartner courierPartner = findByUuid(uuid);
        courierPartner.setActive(true);
        courierPartner.setModifiedAt(LocalDateTime.now());
        courierPartnerRepository.save(courierPartner);
    }

    @Override
    public void inactivate(String uuid) throws BadRequestException {
        CourierPartner courierPartner = findByUuid(uuid);
        courierPartner.setActive(false);
        courierPartner.setModifiedAt(LocalDateTime.now());
        courierPartnerRepository.save(courierPartner);
    }

    @Override
    public void delete(String uuid) throws BadRequestException {
        CourierPartner courierPartner = findByUuid(uuid);
        courierPartner.setActive(false);
        courierPartner.setDeleted(true);
        courierPartnerRepository.save(courierPartner);
    }

    @Override
    public void revive(String uuid) throws BadRequestException {
        CourierPartner courierPartner = findByUuid(uuid);
        courierPartner.setDeleted(false);
        courierPartner.setModifiedAt(LocalDateTime.now());
        courierPartnerRepository.save(courierPartner);
    }

    @Override
    public List<KeyValueDto> listInKeyValue() throws BadRequestException {
        List<CourierPartner> courierPartnerList = courierPartnerRepository.findByActiveAndDeleted(true,false);
        return courierPartnerList.stream()
                .map(courierPartner -> CourierPartnerMapper.MAPPER.mapToKeyPairDto(courierPartner))
                .collect(Collectors.toList());
    }


    public List<CourierPartnerDto.DetailCourierPartner> courierPartnerList(String data) {
        // Data >  Active | Inactive | Deleted | All
        List<CourierPartner> list = null;
        if (data.equals("Active")) {
            list = courierPartnerRepository.findByActiveAndDeleted(true, false);
        } else if (data.equals("Inactive")) {
            list = courierPartnerRepository.findByActiveAndDeleted(false, false);
        } else if (data.equals("Deleted")) {
            list = courierPartnerRepository.findByDeleted(true);
        } else {
            list = courierPartnerRepository.findAll();
        }
        return list.stream().map(CourierPartnerMapper.MAPPER::mapToCourierPartnerDetailDto).toList();
    }

}
