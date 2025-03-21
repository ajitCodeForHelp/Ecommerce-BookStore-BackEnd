package com.bt.ecommerce.primary.service;

import com.bt.ecommerce.bean.DataTableResponsePacket;
import com.bt.ecommerce.bean.KeyValueDto;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.dto.AbstractDto;
import com.bt.ecommerce.primary.dto.CouponCodeDto;
import com.bt.ecommerce.primary.mapper.CouponCodeMapper;
import com.bt.ecommerce.primary.pojo.CouponCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class CouponCodeService extends _BaseService implements _BaseServiceImpl {
    @Override
    public String save(AbstractDto.Save saveDto) throws BadRequestException {
        CouponCodeDto.SaveCoupon saveCoupon = (CouponCodeDto.SaveCoupon) saveDto;
        CouponCode couponCode = CouponCodeMapper.MAPPER.mapToSaveCouponCode(saveCoupon);
        couponCodeRepository.save(couponCode);
        return couponCode.getUuid();
    }

    @Override
    public void update(String uuid, AbstractDto.Update updateDto) throws BadRequestException {
        CouponCodeDto.UpdateCoupon updateCoupon = (CouponCodeDto.UpdateCoupon) updateDto;
        CouponCode couponCode = findByUuid(uuid);
        mapToUpdateCouponCode(couponCode, updateCoupon);
        couponCodeRepository.save(couponCode);
    }

    private void mapToUpdateCouponCode(CouponCode couponCode, CouponCodeDto.UpdateCoupon updateCoupon) {
        couponCode.setTitle(updateCoupon.getTitle());
        couponCode.setCouponCode(updateCoupon.getCouponCode());
        couponCode.setDescription(updateCoupon.getDescription());
        couponCode.setStartDate(updateCoupon.getStartDate());
        couponCode.setEndDate(updateCoupon.getEndDate());
        couponCode.setDiscountType(updateCoupon.getDiscountType());
        couponCode.setMinOrderValue(updateCoupon.getMinOrderValue());
        couponCode.setDiscountValue(updateCoupon.getDiscountValue());
        couponCode.setMaxDiscountAmount(updateCoupon.getMaxDiscountAmount());
        couponCode.setMaxUsePerUser(updateCoupon.getMaxUsePerUser());
        couponCode.setUsedCount(updateCoupon.getUsedCount());
        couponCode.setReferralCoupon(updateCoupon.getReferralCoupon());
        couponCode.setShippingCoupon(updateCoupon.getShippingCoupon());

    }

    @Override
    public AbstractDto.Detail get(String uuid) throws BadRequestException {
        CouponCode couponCode = findByUuid(uuid);
        return CouponCodeMapper.MAPPER.mapToCouponCodeDetailDto(couponCode);
    }

    @Override
    public DataTableResponsePacket list(Boolean deleted, Integer pageNumber, Integer pageSize, String search) {
        return null;
    }

    @Override
    public void activate(String uuid) throws BadRequestException {
        CouponCode couponCode = findByUuid(uuid);
        couponCode.setActive(true);
        couponCode.setModifiedAt(LocalDateTime.now());
        couponCodeRepository.save(couponCode);
    }

    @Override
    public void inactivate(String uuid) throws BadRequestException {
        CouponCode couponCode = findByUuid(uuid);
        couponCode.setActive(false);
        couponCode.setModifiedAt(LocalDateTime.now());
        couponCodeRepository.save(couponCode);
    }

    @Override
    public void delete(String uuid) throws BadRequestException {
        CouponCode couponCode = findByUuid(uuid);
        couponCode.setActive(false);
        couponCode.setDeleted(true);
        couponCodeRepository.save(couponCode);
    }

    @Override
    public void revive(String uuid) throws BadRequestException {
        CouponCode couponCode = findByUuid(uuid);
        couponCode.setDeleted(false);
        couponCode.setModifiedAt(LocalDateTime.now());
        couponCodeRepository.save(couponCode);
    }

    @Override
    public List<KeyValueDto> listInKeyValue() throws BadRequestException {
        return null;
    }

    public List<CouponCodeDto.DetailCoupon> couponList(String data) {
        // Data >  Active | Inactive | Deleted | All
        List<CouponCode> list = null;
        if (data.equals("Active")) {
            list = couponCodeRepository.findByActiveAndDeleted(true, false);
        } else if (data.equals("Inactive")) {
            list = couponCodeRepository.findByActiveAndDeleted(false, false);
        } else if (data.equals("Deleted")) {
            list = couponCodeRepository.findByDeleted(true);
        } else {
            list = couponCodeRepository.findAll();
        }
        return list.stream().map(CouponCodeMapper.MAPPER::mapToCouponCodeDetailDto).toList();
    }

    public List<CouponCodeDto.DetailCoupon> couponListForCustomer() {
        // Only currently active coupon will be send to the frontend data and later on we will be done
        List<CouponCode> couponCodes = couponCodeRepository.findActiveCouponCodeList(LocalDate.now());
        return couponCodes.stream().map(CouponCodeMapper.MAPPER::mapToCouponCodeDetailDto).toList();
    }

    private CouponCode findByUuid(String uuid) throws BadRequestException {
        CouponCode couponCode = couponCodeRepository.findByUuid(uuid);
        if (couponCode == null) {
            throw new BadRequestException("ecommerce.common.message.record_not_exist");
        }
        return couponCode;
    }
}
