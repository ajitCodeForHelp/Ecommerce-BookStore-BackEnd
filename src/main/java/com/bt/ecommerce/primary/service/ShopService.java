package com.bt.ecommerce.primary.service;

import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.dto.ShopDto;
import com.bt.ecommerce.primary.mapper.ShopMapper;
import com.bt.ecommerce.primary.pojo.Shop;
import com.bt.ecommerce.primary.pojo.common.BasicParent;
import com.bt.ecommerce.primary.pojo.user.UserAdmin;
import com.bt.ecommerce.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ShopService extends _BaseService {

    public String save(ShopDto.Save shopDto) throws BadRequestException {
        Shop shop = ShopMapper.MAPPER.mapToPojo(shopDto);
        UserAdmin userAdmin = new UserAdmin();
        boolean isMobileExist = userAdminRepository.existsByMobile(shopDto.getShopOwnerDto().getMobile());
        if (isMobileExist) {
            throw new BadRequestException("Record Already Exist With Given Mobile No.");
        }
        boolean isEmailExist = userAdminRepository.existsByEmail(shopDto.getShopOwnerDto().getEmail());
        if (isEmailExist) {
            throw new BadRequestException("Record Already Exist With Given Email Id");
        }
        userAdmin.setFirstName(shopDto.getShopOwnerDto().getFirstName());
        userAdmin.setLastName(shopDto.getShopOwnerDto().getLastName());
        userAdmin.setEmail(shopDto.getShopOwnerDto().getEmail());
        userAdmin.setMobile(shopDto.getShopOwnerDto().getMobile());
        userAdmin.setPwdText(shopDto.getShopOwnerDto().getPwdText());
        userAdmin.setPwdSecure(TextUtils.getEncodedPassword(shopDto.getShopOwnerDto().getPwdText()));
        userAdmin.setPhotoImageUrl(shopDto.getShopOwnerDto().getPhotoImageUrl());
        userAdminRepository.save(userAdmin);

        shop.setShopOwnerId(userAdmin.getId());
        shop.setShopOwnerDetail(new BasicParent(userAdmin.getUuid(), userAdmin.fullName()));
        shopRepository.save(shop);

        userAdmin.setAssociatedShopId(shop.getId());
        userAdmin.setAssociatedShopDetail(new BasicParent(shop.getUuid(), shop.getTitle()));
        return shop.getUuid();
    }
}