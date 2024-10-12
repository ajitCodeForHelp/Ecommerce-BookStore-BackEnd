package com.bt.ecommerce.primary.service;

import com.bt.ecommerce.primary.pojo.enums.GenderEnum;
import com.bt.ecommerce.primary.pojo.enums.RoleEnum;
import com.bt.ecommerce.primary.pojo.user.SystemUser;
import com.bt.ecommerce.utils.TextUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class BaseDataService extends _BaseService {

    @PostConstruct
    private void postConstruct() {
        generateDefaultAdmins();
    }

    public void generateDefaultAdmins() {
        {
            String superAdminEmail = "super@admin.com";
            SystemUser userAdmin = systemUserRepository.findFirstByUsername(superAdminEmail);
            if (userAdmin == null) {
                userAdmin = new SystemUser();
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
                systemUserRepository.save(userAdmin);
            }
        }

        {
            String adminEmail = "admin@admin.com";
            SystemUser userAdmin = systemUserRepository.findFirstByUsername(adminEmail);
            if (userAdmin == null) {
                userAdmin = new SystemUser();
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
                systemUserRepository.save(userAdmin);
            }
        }
    }
}