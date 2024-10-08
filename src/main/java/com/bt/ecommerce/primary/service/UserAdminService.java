package com.bt.ecommerce.primary.service;

import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.dto.AuthDto;
import com.bt.ecommerce.primary.pojo.user.UserAdmin;
import com.bt.ecommerce.primary.pojo.user._BaseUser;
import com.bt.ecommerce.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class UserAdminService extends _BaseService {


    public UserAdmin findByUsername(String userName) throws BadRequestException {
        UserAdmin pojo = userAdminRepository.findFirstByUsername(userName);
        if (pojo == null) {
            throw new BadRequestException("ecommerce.common.message.record_not_exist");
        }
        return pojo;
    }

    public boolean checkUserExistWithMobile(String isdCode, String mobile) {
        return userAdminRepository.existsByIsdCodeAndMobile(isdCode, mobile);
    }

    public boolean checkUserExistWithEmail(String email) {
        return userAdminRepository.existsByEmail(email);
    }

    public UserAdmin findFirstByIsdAndMobileAndId(String isdCode, String mobile, ObjectId id) {
        return userAdminRepository.findFirstByIsdAndMobileAndId(isdCode, mobile, id);
    }

    public UserAdmin findFirstByEmailAndId(String email, ObjectId id) {
        return userAdminRepository.findFirstByEmailAndId(email, id);
    }

    public List<UserAdmin> findByActiveAndDeleted(boolean active, boolean deleted) {
        return userAdminRepository.findByActiveAndDeleted(active, deleted);
    }

    public Page<UserAdmin> findByDeleted(boolean deleted, Integer pageNumber, Integer pageSize, String search) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        return userAdminRepository.findByDeleted(deleted, search, pageable);
    }

    public void save(UserAdmin userAdmin) {
        userAdminRepository.save(userAdmin);
    }



}