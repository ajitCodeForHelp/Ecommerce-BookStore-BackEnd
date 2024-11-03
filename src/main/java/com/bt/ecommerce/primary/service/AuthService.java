package com.bt.ecommerce.primary.service;

import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.dto.AuthDto;
import com.bt.ecommerce.primary.pojo.user.Customer;
import com.bt.ecommerce.primary.pojo.user.SystemUser;
import com.bt.ecommerce.primary.pojo.user._BaseUser;
import com.bt.ecommerce.security.JwtTokenUtil;
import com.bt.ecommerce.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Service
public class AuthService extends _BaseService {
    @Autowired protected JwtTokenUtil jwtTokenUtil;
    public SystemUser findByUsername(String userName) throws BadRequestException {
        SystemUser pojo = systemUserRepository.findFirstByUsername(userName);
        if (pojo == null) {
            throw new BadRequestException("ecommerce.common.message.record_not_exist");
        }
        return pojo;
    }

    public Customer findByCustomerUsername(String username) throws BadRequestException {
        Customer customer = customerRepository.findFirstByUsername(username);
        if(customer == null){
            throw new BadRequestException("ecommerce.common.message.record_not_exist");
        }
        return customer;
    }

    public AuthDto.UserDetails loginAdmin(String userName, String password, String ipAddress) throws BadRequestException {
        SystemUser userAdmin = findByUsername(userName);
        validateUser(userAdmin, password);
        userAdmin.setLastLogin(LocalDateTime.now());
        systemUserRepository.save(userAdmin);
        return generateAuthTokenAndGetUserDetails(userAdmin, ipAddress);
    }

//    public AuthDto.UserDetails loginCustomer(String userName, String password, String ipAddress) throws BadRequestException {
//        Customer userCustomer = findByCustomerUsername(userName);
//        validateUser(userCustomer, password);
//        userCustomer.setLastLogin(LocalDateTime.now());
//        customerRepository.save(userCustomer);
//        return generateAuthTokenAndGetUserDetails(userCustomer, ipAddress);
//    }

    private void validateUser(_BaseUser user, String password) throws BadRequestException {
        if (!user.isActive() || user.isDeleted()) {
            throw new BadRequestException("ecommerce.common.message.user_disable");
        }
        if (!TextUtils.matchPassword(password, user.getPwdSecure())) {
            throw new BadRequestException("Invalid Credential");
        }
        // We need to implement Multi Device Login as well.
        if (TextUtils.isEmpty(user.getUniqueKey())) {
            user.setUniqueKey(TextUtils.getUniqueKey());
        }
    }

    private AuthDto.UserDetails generateAuthTokenAndGetUserDetails(_BaseUser user, String ipAddress) {
        // Replace With Mapper.
        AuthDto.UserDetails adminDetail = new AuthDto.UserDetails();
        adminDetail.setFirstName(user.getFirstName());
        adminDetail.setLastName(user.getLastName());
        adminDetail.setUserType(user.getUserType());

        String token = TextUtils.md5encryption(user.getUniqueKey()) + TextUtils.md5encryption(ipAddress);
        Map<String, Object> bodyPart = new LinkedHashMap<>();
        bodyPart.put("username", user.getUsername());
        bodyPart.put("u-id", user.getUuid());
        bodyPart.put("u-ty", user.getUserType().toString());
        bodyPart.put("d-id", user.getUuid());
        bodyPart.put("token", token);
        adminDetail.setSecretKey(jwtTokenUtil.generateToken(bodyPart, user));
        return adminDetail;
    }


}
