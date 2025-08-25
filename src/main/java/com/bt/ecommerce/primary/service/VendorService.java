package com.bt.ecommerce.primary.service;

import com.bt.ecommerce.bean.DataTableResponsePacket;
import com.bt.ecommerce.bean.KeyValueDto;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.dto.AbstractDto;
import com.bt.ecommerce.primary.dto.VendorDto;
import com.bt.ecommerce.primary.mapper.VendorMapper;
import com.bt.ecommerce.primary.pojo.Vendor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class VendorService extends _BaseService implements _BaseServiceImpl{
    @Override
    public String save(AbstractDto.Save saveDto) throws BadRequestException {
        VendorDto.SaveVendor saveVendor = (VendorDto.SaveVendor) saveDto;
        Vendor vendor = VendorMapper.MAPPER.mapToPojo(saveVendor);
        vendorRepository.save(vendor);
        return vendor.getUuid();
    }

    @Override
    public void update(String uuid, AbstractDto.Update updateDto) throws BadRequestException {
        VendorDto.UpdateVendor updateVendor = (VendorDto.UpdateVendor) updateDto;
        Vendor vendor = findByUuid(uuid);
        vendor =  VendorMapper.MAPPER.mapToPojo(vendor , updateVendor);
        vendorRepository.save(vendor);
    }

    private Vendor findByUuid(String uuid) throws BadRequestException {
        Vendor vendor = vendorRepository.findByUuid(uuid);
        if (vendor == null) {
            throw new BadRequestException("ecommerce.common.message.record_not_exist");
        }
        return vendor;
    }

    @Override
    public AbstractDto.Detail get(String uuid) throws BadRequestException {
        Vendor vendor = findByUuid(uuid);
        return VendorMapper.MAPPER.mapToVendorDetailDto(vendor);
    }

    @Override
    public DataTableResponsePacket list(Boolean deleted, Integer pageNumber, Integer pageSize, String search) {
        return null;
    }

    @Override
    public void activate(String uuid) throws BadRequestException {
        Vendor vendor = findByUuid(uuid);
        vendor.setActive(true);
        vendor.setModifiedAt(LocalDateTime.now());
        vendorRepository.save(vendor);
    }

    @Override
    public void inactivate(String uuid) throws BadRequestException {
        Vendor vendor = findByUuid(uuid);
        vendor.setActive(false);
        vendor.setModifiedAt(LocalDateTime.now());
        vendorRepository.save(vendor);
    }

    @Override
    public void delete(String uuid) throws BadRequestException {
        Vendor vendor = findByUuid(uuid);
        vendor.setActive(false);
        vendor.setDeleted(true);
        vendorRepository.save(vendor);
    }

    @Override
    public void revive(String uuid) throws BadRequestException {
        Vendor vendor = findByUuid(uuid);
        vendor.setDeleted(false);
        vendor.setModifiedAt(LocalDateTime.now());
        vendorRepository.save(vendor);
    }

    @Override
    public List<KeyValueDto> listInKeyValue() throws BadRequestException {
        List<Vendor> vendorList = vendorRepository.findByActiveAndDeleted(true,false);
        return vendorList.stream()
                .map(vendor -> VendorMapper.MAPPER.mapToKeyPairDto(vendor))
                .collect(Collectors.toList());
    }


    public List<VendorDto.DetailVendor> vendorList(String data) {
        // Data >  Active | Inactive | Deleted | All
        List<Vendor> list = null;
        if (data.equals("Active")) {
            list = vendorRepository.findByActiveAndDeleted(true, false);
        } else if (data.equals("Inactive")) {
            list = vendorRepository.findByActiveAndDeleted(false, false);
        } else if (data.equals("Deleted")) {
            list = vendorRepository.findByDeleted(true);
        } else {
            list = vendorRepository.findAll();
        }
        return list.stream().map(VendorMapper.MAPPER::mapToVendorDetailDto).toList();
    }
}
