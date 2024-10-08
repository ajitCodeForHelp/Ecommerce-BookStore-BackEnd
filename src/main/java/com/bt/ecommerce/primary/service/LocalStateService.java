//package com.kpis.kpisFood.primary.service;
//
//import com.kpis.kpisFood.primary.pojo.location.LocalState;
//import org.bson.types.ObjectId;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class LocalStateService extends AbstractService<LocalState, LocalStateRepository> {
//
//    public LocalStateService(LocalStateRepository localStateRepository) {
//        super(localStateRepository);
//    }
//
//    public List<LocalState> findStateByCountryId(ObjectId countryId) {
//        return localStateRepository.findByCountryId(countryId);
//    }
//
//    public LocalState findByTitleAndNotCountryId(String title, ObjectId countryId) {
//        return localStateRepository.findByTitleAndNotCountryId(title, countryId);
//    }
//}