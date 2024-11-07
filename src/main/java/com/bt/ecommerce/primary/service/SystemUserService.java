package com.bt.ecommerce.primary.service;

import com.bt.ecommerce.bean.DataTableResponsePacket;
import com.bt.ecommerce.bean.KeyValueDto;
import com.bt.ecommerce.configuration.SpringBeanContext;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.dto.AbstractDto;
import com.bt.ecommerce.primary.dto.StaffDto;
import com.bt.ecommerce.primary.mapper.SystemUserMapper;
import com.bt.ecommerce.primary.pojo.common.BasicParent;
import com.bt.ecommerce.primary.pojo.enums.RoleEnum;
import com.bt.ecommerce.primary.pojo.user.SystemUser;
import com.bt.ecommerce.security.JwtUserDetailsService;
import com.bt.ecommerce.utils.TextUtils;
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
public class SystemUserService extends _BaseService implements _BaseServiceImpl {

    @Override
    public String save(AbstractDto.Save save) throws BadRequestException {
        SystemUser loggedInUser = (SystemUser) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        StaffDto.SaveStaff saveStaffDto = (StaffDto.SaveStaff) save;
        SystemUser staff = SystemUserMapper.MAPPER.mapToPojo(saveStaffDto);
        if (checkUserExistWithMobile(saveStaffDto.getIsdCode(), saveStaffDto.getMobile())) {
            throw new BadRequestException("kpis_food.common.message.mobile_already_exist");
        }
        if (checkUserExistWithEmail(saveStaffDto.getEmail())) {
            throw new BadRequestException("kpis_food.common.message.email_already_exist");
        }
        staff.setParentAdminId(loggedInUser.getParentAdminId());
        staff.setParentAdminDetail(new BasicParent(loggedInUser.getUuid(), loggedInUser.fullName()));

        staff.setEmail(saveStaffDto.getEmail());
        staff.setUsername(saveStaffDto.getEmail());
        staff.setPwdText(saveStaffDto.getPassword());
        staff.setPwdSecure(TextUtils.getEncodedPassword(saveStaffDto.getPassword()));
        staff.setUserType(RoleEnum.ROLE_SUB_ADMIN);
        staff.setUniqueKey(TextUtils.getUniqueKey());
        staff = systemUserRepository.save(staff);
        return staff.getUuid();
    }

    @Override
    public void update(String uuid, AbstractDto.Update update) throws BadRequestException {
        StaffDto.UpdateStaff updateStaffDto = (StaffDto.UpdateStaff) update;
        SystemUser staff = findByUuid(uuid);
        staff = SystemUserMapper.MAPPER.mapToPojo(staff, updateStaffDto);
        {
            SystemUser userExistWithMobile = findFirstByIsdAndMobileAndId(updateStaffDto.getIsdCode(), updateStaffDto.getMobile(), staff.getId());
            if (userExistWithMobile != null) {
                throw new BadRequestException("kpis_food.common.message.mobile_already_exist");
            }
        }
        {
            SystemUser userExistWithEmail = findFirstByEmailAndId(updateStaffDto.getEmail(), staff.getId());
            if (userExistWithEmail != null) {
                throw new BadRequestException("kpis_food.common.message.mobile_already_exist");
            }
        }
        staff.setEmail(updateStaffDto.getEmail());
        staff.setUsername(updateStaffDto.getEmail());
        staff.setUserType(RoleEnum.ROLE_SUB_ADMIN);
        staff.setUniqueKey(TextUtils.getUniqueKey());
        staff = systemUserRepository.save(staff);
    }

    @Override
    public AbstractDto.Detail get(String uuid) throws BadRequestException {
        SystemUser staff = findByUuid(uuid);
        return SystemUserMapper.MAPPER.mapToDetailDto(staff);
    }

    public List<StaffDto.DetailStaff> listData(String data) {
        SystemUser loggedInUser = (SystemUser) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        // Data >  Active | Inactive | Deleted | All
        List<SystemUser> list = null;
        if(data.equals("Active")){
            list = systemUserRepository.findByParentAdminIdAndActiveAndDeleted(loggedInUser.getId(), true, false);
        } else if (data.equals("Inactive")) {
            list = systemUserRepository.findByParentAdminIdAndActiveAndDeleted(loggedInUser.getId(), false, false);
        } else if (data.equals("Deleted")) {
            list = systemUserRepository.findByParentAdminIdAndDeleted(loggedInUser.getId(), true);
        } else {
            list = systemUserRepository.findAll();
        }
        return list.stream()
                .map(staff -> SystemUserMapper.MAPPER.mapToDetailDto(staff))
                .collect(Collectors.toList());
    }

    @Override
    public DataTableResponsePacket list(Boolean deleted, Integer pageNumber, Integer pageSize, String search) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<SystemUser> pageData = systemUserRepository.findByDeleted(deleted, search, pageable);
        return getDataTableResponsePacket(pageData, pageData.getContent().stream()
                .map(staff -> SystemUserMapper.MAPPER.mapToDetailDto(staff))
                .collect(Collectors.toList()));
    }

    @Override
    public void activate(String uuid) throws BadRequestException {
        SystemUser staff = findByUuid(uuid);
        staff.setActive(true);
        staff.setModifiedAt(LocalDateTime.now());
        systemUserRepository.save(staff);
    }

    @Override
    public void inactivate(String uuid) throws BadRequestException {
        SystemUser staff = findByUuid(uuid);
        staff.setActive(false);
        staff.setModifiedAt(LocalDateTime.now());
        systemUserRepository.save(staff);
    }

    @Override
    public void delete(String uuid) throws BadRequestException {
        SystemUser staff = findByUuid(uuid);
        staff.setDeleted(true);
        staff.setActive(false);
        systemUserRepository.save(staff);
    }

    @Override
    public void revive(String uuid) throws BadRequestException {
        SystemUser systemUser = findByUuid(uuid);
        systemUser.setDeleted(false);
        systemUser.setModifiedAt(LocalDateTime.now());
        systemUserRepository.save(systemUser);
    }

    public List<KeyValueDto> listInKeyValue() {
        List<SystemUser> staffList = systemUserRepository.findByActiveAndDeleted();
        return staffList.stream()
                .map(staff -> SystemUserMapper.MAPPER.mapToKeyPairDto(staff))
                .collect(Collectors.toList());
    }

    private SystemUser findByUuid(String uuid) throws BadRequestException {
        SystemUser staff = systemUserRepository.findByUuid(uuid);
        if (staff == null) {
            throw new BadRequestException("ecommerce.common.message.record_not_exist");
        }
        return staff;
    }


    public SystemUser findByUsername(String userName) throws BadRequestException {
        SystemUser pojo = systemUserRepository.findFirstByUsername(userName);
        if (pojo == null) {
            throw new BadRequestException("ecommerce.common.message.record_not_exist");
        }
        return pojo;
    }

    public boolean checkUserExistWithMobile(String isdCode, String mobile) {
        return systemUserRepository.existsByIsdCodeAndMobile(isdCode, mobile);
    }

    public boolean checkUserExistWithEmail(String email) {
        return systemUserRepository.existsByEmail(email);
    }

    public SystemUser findFirstByIsdAndMobileAndId(String isdCode, String mobile, ObjectId id) {
        return systemUserRepository.findFirstByIsdAndMobileAndId(isdCode, mobile, id);
    }

    public SystemUser findFirstByEmailAndId(String email, ObjectId id) {
        return systemUserRepository.findFirstByEmailAndId(email, id);
    }

    public List<SystemUser> findByActiveAndDeleted() {
        return systemUserRepository.findByActiveAndDeleted();
    }

    public Page<SystemUser> findByDeleted(boolean deleted, Integer pageNumber, Integer pageSize, String search) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        return systemUserRepository.findByDeleted(deleted, search, pageable);
    }

    public void save(SystemUser userAdmin) {
        systemUserRepository.save(userAdmin);
    }
}