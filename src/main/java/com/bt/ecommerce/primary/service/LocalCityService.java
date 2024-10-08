//package com.kpis.kpisFood.primary.service;
//
//import com.kpis.kpisFood.primary.pojo.location.LocalCity;
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
//public class LocalCityService extends AbstractService<LocalCity, LocalCityRepository> {
//    public LocalCityService(LocalCityRepository localCityRepository) {
//        super(localCityRepository);
//    }
//
//    @Override
//    public Page<LocalCity> findByDeleted(boolean deleted, Integer pageNumber, Integer pageSize, String search) {
//        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
//        return localCityRepository.findByDeleted(deleted, search, pageable);
//    }
//
//    public List<LocalCity> findCityByStateId(ObjectId stateId) {
//        return localCityRepository.findByStateId(stateId);
//    }
//
//    public LocalCity findByTitleAndNotStateId(String title, ObjectId stateId) {
//        return localCityRepository.findByTitleAndNotStateId(title, stateId);
//    }
//}