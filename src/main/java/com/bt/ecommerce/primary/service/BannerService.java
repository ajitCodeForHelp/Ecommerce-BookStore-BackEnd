package com.bt.ecommerce.primary.service;

import com.bt.ecommerce.bean.DataTableResponsePacket;
import com.bt.ecommerce.bean.KeyValueDto;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.dto.AbstractDto;
import com.bt.ecommerce.primary.dto.BannerDto;
import com.bt.ecommerce.primary.mapper.BannerMapper;
import com.bt.ecommerce.primary.pojo.Banner;
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
public class BannerService extends _BaseService implements _BaseServiceImpl {

    @Override
    public String save(AbstractDto.Save save) throws BadRequestException {
        BannerDto.CreateBanner saveBannerDto = (BannerDto.CreateBanner) save;
        Banner banner = BannerMapper.MAPPER.mapToPojo(saveBannerDto);
        banner = bannerRepository.save(banner);
        return banner.getUuid();

    }

    @Override
    public void update(String uuid, AbstractDto.Update update) throws BadRequestException {
        BannerDto.UpdateDetail updateBannerDto = (BannerDto.UpdateDetail) update;
        Banner banner = findByUuid(uuid);
        banner = BannerMapper.MAPPER.mapToPojo(banner, updateBannerDto);
        bannerRepository.save(banner);
    }

    @Override
    public AbstractDto.Detail get(String uuid) throws BadRequestException {
        Banner banner = findByUuid(uuid);
        return BannerMapper.MAPPER.mapToPojo(banner);
    }


    @Override
    public DataTableResponsePacket list(Boolean deleted, Integer pageNumber, Integer pageSize, String search) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Banner> pageData = bannerRepository.findByDeleted(deleted, search, pageable);
        return getDataTableResponsePacket(pageData, pageData.getContent().stream()
                .map(banner -> BannerMapper.MAPPER.mapToPojo(banner))
                .collect(Collectors.toList()));
    }

    public List<BannerDto.DetailBanner> list(String data) {
        // Data >  Active | Inactive | Deleted | All
        List<Banner> list = null;
        if (data.equals("Active")) {
            list = bannerRepository.findByActiveAndDeleted(true, false);
        } else if (data.equals("Inactive")) {
            list = bannerRepository.findByActiveAndDeleted(false, false);
        } else if (data.equals("Deleted")) {
            list = bannerRepository.findByDeleted(true);
        } else {
            list = bannerRepository.findAll();
        }
        return list.stream()
                .map(banner -> BannerMapper.MAPPER.mapToPojo(banner))
                .collect(Collectors.toList());
    }


    @Override
    public void activate(String uuid) throws BadRequestException {
        Banner banner = findByUuid(uuid);
        banner.setActive(true);
        banner.setModifiedAt(LocalDateTime.now());
        bannerRepository.save(banner);
    }

    @Override
    public void inactivate(String uuid) throws BadRequestException {
        Banner banner = findByUuid(uuid);
        banner.setActive(false);
        banner.setModifiedAt(LocalDateTime.now());
        bannerRepository.save(banner);
    }

    @Override
    public void delete(String uuid) throws BadRequestException {
        Banner banner = findByUuid(uuid);
        banner.setDeleted(true);
        banner.setActive(false);
        banner.setModifiedAt(LocalDateTime.now());
        bannerRepository.save(banner);
    }

    @Override
    public void revive(String uuid) throws BadRequestException {
        Banner banner = findByUuid(uuid);
        banner.setDeleted(false);
        banner.setModifiedAt(LocalDateTime.now());
        bannerRepository.save(banner);
    }

    @Override
    public List<KeyValueDto> listInKeyValue() throws BadRequestException {
        return null;
    }


    private Banner findByUuid(String uuid) throws BadRequestException {
        Banner banner = bannerRepository.findByUuid(uuid);
        if (banner == null) {
            throw new BadRequestException("ecommerce.common.message.record_not_exist");
        }
        return banner;
    }

    private Banner findById(ObjectId id) throws BadRequestException {
        Banner banner = bannerRepository.findById(id).orElse(null);
        if (banner == null) {
            throw new BadRequestException("ecommerce.common.message.record_not_exist");
        }
        return banner;
    }
}