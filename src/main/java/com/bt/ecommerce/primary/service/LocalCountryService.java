//package com.kpis.kpisFood.primary.service;
//
//import com.kpis.kpisFood.primary.pojo.location.LocalCountry;
//import org.bson.types.ObjectId;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class LocalCountryService extends AbstractService<LocalCountry, LocalCountryRepository> {
//    public LocalCountryService(LocalCountryRepository localCountryRepository) {
//        super(localCountryRepository);
//    }
//
//    @Override
//    public Page<LocalCountry> findByDeleted(boolean deleted, Integer pageNumber, Integer pageSize, String search) {
//        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
//        return localCountryRepository.findByDeleted(deleted, search, pageable);
//    }
//
//    public List<LocalCountry> findAllCountryList() {
//        return localCountryRepository.findAllCountryList();
//    }
//
//    public LocalCountry findByTitle(String title) {
//        return localCountryRepository.findFirstByTitle(title);
//    }
//
//    public LocalCountry findByTitleAndNotId(String title, ObjectId id) {
//        return localCountryRepository.findByTitleAndNotId(title, id);
//    }
//}