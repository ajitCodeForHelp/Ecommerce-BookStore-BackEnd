package com.bt.ecommerce.primary.service;

import com.bt.ecommerce.primary.pojo.enums.GenderEnum;
import com.bt.ecommerce.primary.pojo.enums.RoleEnum;
import com.bt.ecommerce.primary.pojo.user.UserAdmin;
import com.bt.ecommerce.utils.TextUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class BaseDataService extends _BaseService {

    @PostConstruct
    private void postConstruct() {
        generateDefaultAdmins();
//        SpringBeanContext.getBean(ThemeService.class).createDefaultTheme();
//        SpringBeanContext.getBean(LanguageService.class).createDefaultLanguage();
//        SpringBeanContext.getBean(CurrencyService.class).createDefaultCurrency();
//        generateDefaultLocation();
//        generateDefaultClient();
    }

    public void generateDefaultAdmins() {
        {
            String superAdminEmail = "super@admin.com";
            UserAdmin userAdmin = userAdminRepository.findFirstByUsername(superAdminEmail);
            if (userAdmin == null) {
                userAdmin = new UserAdmin();
                userAdmin.setFirstName("SuperAdmin");
                userAdmin.setLastName("SuperAdmin");
                userAdmin.setGender(GenderEnum.Male);
                userAdmin.setIsdCode("+91");
                userAdmin.setMobile("9999999999");
                userAdmin.setEmail(superAdminEmail);
                userAdmin.setUsername(userAdmin.getEmail());
                userAdmin.setPwdSecure(TextUtils.getEncodedPassword("12"));
                userAdmin.setPwdText("12");
                userAdmin.setUserType(RoleEnum.ROLE_SUPER_ADMIN);
                userAdmin.setUniqueKey(TextUtils.getUniqueKey());
                userAdminRepository.save(userAdmin);
            }
        }

        {
            String adminEmail = "admin@admin.com";
            UserAdmin userAdmin = userAdminRepository.findFirstByUsername(adminEmail);
            if (userAdmin == null) {
                userAdmin = new UserAdmin();
                userAdmin.setFirstName("Admin");
                userAdmin.setLastName("Admin");
                userAdmin.setGender(GenderEnum.Male);
                userAdmin.setIsdCode("+91");
                userAdmin.setMobile("8888888888");
                userAdmin.setEmail(adminEmail);
                userAdmin.setUsername(userAdmin.getEmail());
                userAdmin.setPwdSecure(TextUtils.getEncodedPassword("12"));
                userAdmin.setPwdText("12");
                userAdmin.setUserType(RoleEnum.ROLE_ADMIN);
                userAdmin.setUniqueKey(TextUtils.getUniqueKey());
                userAdminRepository.save(userAdmin);
            }
        }
    }

//    public void generateDefaultLocation() {
//        LocalCountry localCountry = localCountryRepository.findFirstByTitle(ProjectConst.DEFAULT_COUNTRY);
//        {
//            if (localCountry == null) {
//                localCountry = LocalCountry.builder()
//                        .title(ProjectConst.DEFAULT_COUNTRY)
//                        .phoneCode(ProjectConst.DEFAULT_COUNTRY_PHONE_CODE)
//                        .shortCode(ProjectConst.DEFAULT_COUNTRY_SHORT_CODE)
//                        .currencyDetail(currencyRepository.findByCurrencyTitle(ProjectConst.DEFAULT_CURRENCY))
//                        .languageDetail(languageRepository.findByTitle(ProjectConst.DEFAULT_LANGUAGE))
//                        .build();
//                localCountry = localCountryRepository.save(localCountry);
//            }
//        }
//        LocalState localState = localStateRepository.findByCountryIdAndTitle(localCountry.getId(), ProjectConst.DEFAULT_STATE);
//        {
//            if (localState == null) {
//                localState = LocalState.builder()
//                        .title(ProjectConst.DEFAULT_STATE)
//                        .countryId(localCountry.getId())
//                        .countryDetail(new BasicParent(localCountry.getUuid(), localCountry.getTitle()))
//                        .build();
//                localState = localStateRepository.save(localState);
//            }
//        }
//        LocalCity localCity = localCityRepository.findByStateIdAndTitle(localState.getId(), ProjectConst.DEFAULT_CITY);
//        {
//            if (localCity == null) {
//                localCity = LocalCity.builder()
//                        .title(ProjectConst.DEFAULT_CITY)
//                        .countryId(localCountry.getId())
//                        .countryDetail(new BasicParent(localCountry.getUuid(), localCountry.getUuid()))
//                        .stateId(localState.getId())
//                        .stateDetail(new BasicParent(localState.getUuid(), localState.getTitle()))
//                        .build();
//                localCityRepository.save(localCity);
//            }
//        }
//    }

//    public void generateDefaultClient() {
//        String superAdminEmail = "super@admin.com";
//        UserAdmin userAdmin = userAdminRepository.findFirstByUsername(superAdminEmail);
//
//        String superClientEmail = "eatres@client.com";
//        UserClient userClient = userClientRepository.findFirstByUsername(superClientEmail);
//        if (userClient == null) {
//            UserClientDto.SaveUserClient saveUserClientObj = new UserClientDto.SaveUserClient();
//            saveUserClientObj.setFirstName("Eatres");
//            saveUserClientObj.setLastName("Client");
//            saveUserClientObj.setIsdCode("+91");
//            saveUserClientObj.setMobile("9999999999");
//            saveUserClientObj.setEmail(superClientEmail);
//            saveUserClientObj.setPassword("12");
//            saveUserClientObj.setPhotoImageUrl(null);
//            try {
//                SpringBeanContext.getBean(UserClientTrade.class).save(userAdmin, saveUserClientObj);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
}