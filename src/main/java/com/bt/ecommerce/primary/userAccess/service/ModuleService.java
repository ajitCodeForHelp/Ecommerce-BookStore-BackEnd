package com.bt.ecommerce.primary.userAccess.service;

import com.bt.ecommerce.bean.KeyValueDto;
import com.bt.ecommerce.configuration.SpringBeanContext;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.pojo.common.BasicParent;
import com.bt.ecommerce.primary.pojo.user.SystemUser;
import com.bt.ecommerce.primary.service._BaseService;
import com.bt.ecommerce.primary.userAccess.dto.ModuleDto;
import com.bt.ecommerce.primary.userAccess.dto.UrlDto;
import com.bt.ecommerce.primary.userAccess.mapper.ModuleMapper;
import com.bt.ecommerce.primary.userAccess.mapper.UrlMapper;
import com.bt.ecommerce.primary.userAccess.pojo.Module;
import com.bt.ecommerce.primary.userAccess.pojo.Url;
import com.bt.ecommerce.security.JwtUserDetailsService;
import com.bt.ecommerce.utils.TextUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ModuleService extends _BaseService {

    public String save(ModuleDto.SaveModule saveModuleObj) throws BadRequestException {
        SystemUser loggedInUser = (SystemUser) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        // validate title and convert it to lower case and remove space
        saveModuleObj.setTitle(TextUtils.validateTitle(saveModuleObj.getTitle()));
        Module module = moduleRepository.findByTitle(saveModuleObj.getTitle());
        if (module != null) {
            throw new BadRequestException("ecommerce.common.message.module_already_exist");
        }
        module = ModuleMapper.MAPPER.mapToPojo(saveModuleObj);
        module = moduleRepository.save(module);
        return module.getUuid();
    }

    public void update(String uuid, ModuleDto.UpdateModule updateModuleObj) throws BadRequestException {
        // validate title and convert it to lower case and remove space
        updateModuleObj.setTitle(TextUtils.validateTitle(updateModuleObj.getTitle()));
        Module module = moduleRepository.findByUuid(uuid);
        Module moduleAlreadyExist = moduleRepository.findByTitleAndNotId(updateModuleObj.getTitle(), module.getId());
        if (moduleAlreadyExist != null) {
            throw new BadRequestException("ecommerce.common.message.module_already_exist");
        }
        module = ModuleMapper.MAPPER.mapToPojo(module, updateModuleObj);
        moduleRepository.save(module);
        updateModuleUrlDetail(module);
    }

    public ModuleDto.DetailModule get(String uuid) throws BadRequestException {
        Module module = findByUuid(uuid);
        return mapToDetailDto(module);
    }

    public List<ModuleDto.DetailModule> listData(String data) throws BadRequestException {
        // Data >  Active | Inactive | Deleted | All
        List<Module> list = null;
        if (data.equals("Active")) {
            list = moduleRepository.findByActiveAndDeleted(true, false);
        } else if (data.equals("Inactive")) {
            list = moduleRepository.findByActiveAndDeleted(false, false);
        } else if (data.equals("Deleted")) {
            list = moduleRepository.findByDeleted(true);
        } else {
            list = moduleRepository.findAll();
        }
        return list.stream()
                .map(module -> ModuleMapper.MAPPER.mapToDetailDto(module))
                .collect(Collectors.toList());
    }


    public void activate(String uuid) throws BadRequestException {
        Module module = findByUuid(uuid);
        module.setActive(true);
        module.setModifiedAt(LocalDateTime.now());
        moduleRepository.save(module);
    }


    public void inactivate(String uuid) throws BadRequestException {
        Module module = findByUuid(uuid);
        module.setActive(false);
        module.setModifiedAt(LocalDateTime.now());
        moduleRepository.save(module);
    }


    public void delete(String uuid) throws BadRequestException {
        Module module = findByUuid(uuid);
        module.setDeleted(true);
        module.setActive(false);
        moduleRepository.save(module);
    }


    public void revive(String uuid) throws BadRequestException {
        Module module = findByUuid(uuid);
        module.setDeleted(false);
        module.setModifiedAt(LocalDateTime.now());
        moduleRepository.save(module);
    }

    public List<KeyValueDto> listInKeyValue() {
        List<Module> moduleList = moduleRepository.findAllModuleList();
        return moduleList.stream()
                .map(module -> ModuleMapper.MAPPER.mapToKeyValueDto(module))
                .collect(Collectors.toList());
    }

    private Module findByUuid(String uuid) throws BadRequestException {
        Module module = moduleRepository.findByUuid(uuid);
        if (module == null) {
            throw new BadRequestException("Invalid Record Provided");
        }
        return module;
    }

    // ############################################## Url Operation ####################################################

    public void saveModuleUrls(String moduleUuid, List<UrlDto.SaveUrl> urlDtoList) throws BadRequestException {
        Module module = moduleRepository.findByUuid(moduleUuid);
        if (TextUtils.isEmpty(urlDtoList)) {
            throw new BadRequestException("ecommerce.common.message.invalid_url_data");
        }
        validateModuleUrl(urlDtoList);
        List<Url> urlList = new ArrayList<>();
        for (UrlDto.SaveUrl saveUrlDto : urlDtoList) {
            urlList.add(mapToUrl(module, saveUrlDto));
        }
        urlRepository.saveAll(urlList);
        updateModuleUrlDetail(module);
    }

    public void updateModuleUrl(String moduleUuid, String urlUuid, UrlDto.UpdateUrl updateUrl) throws BadRequestException {
        Module module = moduleRepository.findByUuid(moduleUuid);
        Url url = urlRepository.findByUuid(urlUuid);
        if (!url.getModuleId().equals(module.getId())) {
            throw new BadRequestException("ecommerce.common.message.invalid_url_access");
        }
        url = UrlMapper.MAPPER.mapToPojo(url, updateUrl);
        urlRepository.save(url);
        updateModuleUrlDetail(module);
    }

    public void activateModuleUrl(String moduleUuid, String urlUuid) throws BadRequestException {
        Module module = moduleRepository.findByUuid(moduleUuid);
        Url url = urlRepository.findByUuid(urlUuid);
        if (!url.getModuleId().equals(module.getId())) {
            throw new BadRequestException("ecommerce.common.message.invalid_url_access");
        }
        url.setActive(true);
        urlRepository.save(url);
    }

    public void deactivateModuleUrl(String moduleUuid, String urlUuid) throws BadRequestException {
        Module module = moduleRepository.findByUuid(moduleUuid);
        Url url = urlRepository.findByUuid(urlUuid);
        if (!url.getModuleId().equals(module.getId())) {
            throw new BadRequestException("ecommerce.common.message.invalid_url_access");
        }
        url.setActive(false);
        urlRepository.save(url);
    }

    public void deleteModuleUrl(String moduleUuid, String urlUuid) throws BadRequestException {
        Module module = moduleRepository.findByUuid(moduleUuid);
        Url url = urlRepository.findByUuid(urlUuid);
        if (!url.getModuleId().equals(module.getId())) {
            throw new BadRequestException("ecommerce.common.message.invalid_url_access");
        }
        urlRepository.delete(url);
    }

    private Url mapToUrl(Module module, UrlDto.SaveUrl saveUrlDto) {
        Url url = UrlMapper.MAPPER.mapToPojo(saveUrlDto);
        url.setModuleId(module.getId());
        url.setModuleDetail(new BasicParent(module.getUuid(), module.getTitle()));
        return url;
    }

    private void validateModuleUrl(List<UrlDto.SaveUrl> urlDtoList) throws BadRequestException {
        for (UrlDto.SaveUrl saveUrlDto : urlDtoList) {
            Url url = urlRepository.findByTitle(saveUrlDto.getTitle());
            if (url != null) {
                throw new BadRequestException("ecommerce.common.message.url_already_exist");
            }
        }
    }

    private ModuleDto.DetailModule mapToDetailDto(Module module) {
        ModuleDto.DetailModule detailModule = ModuleMapper.MAPPER.mapToDetailDto(module);
        List<Url> urlList = urlRepository.findByModuleId(module.getId());
        List<Url.UrlRef> urlRefList = null;
        if (!TextUtils.isEmpty(urlList)) {
            urlRefList = new ArrayList<>();
            for (Url url : urlList) {
                urlRefList.add(UrlMapper.MAPPER.mapToDetailRefDto(url));
            }
            detailModule.setUrlList(urlRefList);
        }
        return detailModule;
    }

    public List<ModuleDto.DetailModule> getModuleUrlList() {
        // Only Use During Assign Url To User
        List<Module> moduleList = moduleRepository.findByActiveAndDeleted(true, false);
        if (TextUtils.isEmpty(moduleList)) return null;
        List<ModuleDto.DetailModule> detailModuleList = new ArrayList<>();
        for (Module module : moduleList) {
            ModuleDto.DetailModule detailModule = ModuleMapper.MAPPER.mapToDetailDto(module);
            List<Url> moduleUrlList = urlRepository.findByModuleIdAndActive(module.getId(), true);
            List<Url.UrlRef> urlRefList = moduleUrlList.stream()
                    .map(url -> UrlMapper.MAPPER.mapToRefDto(url))
                    .collect(Collectors.toList());
            detailModule.setUrlList(urlRefList);
            detailModuleList.add(detailModule);
        }
        return detailModuleList;
    }
    private void updateModuleUrlDetail(Module module){
        List<Url> moduleUrlList = urlRepository.findByModuleId(module.getId());
        if(TextUtils.isEmpty(moduleUrlList)) return;
        for (Url url : moduleUrlList) {
           url.setModuleDetail(new BasicParent(module.getUuid(), module.getTitle()));
        }
        urlRepository.saveAll(moduleUrlList);
    }
}