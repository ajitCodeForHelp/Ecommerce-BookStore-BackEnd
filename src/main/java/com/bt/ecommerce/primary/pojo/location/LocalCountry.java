//package com.kpis.kpisFood.primary.pojo.location;
//
//import com.kpis.kpisFood.primary.pojo.Currency;
//import com.kpis.kpisFood.primary.pojo.Language;
//import com.kpis.kpisFood.primary.pojo._BasicEntity;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.Setter;
//import org.springframework.data.mongodb.core.index.CompoundIndex;
//import org.springframework.data.mongodb.core.index.CompoundIndexes;
//import org.springframework.data.mongodb.core.mapping.Document;
//
//@Setter
//@Getter
//@Builder
//@Document(collection = "local_country")
//@CompoundIndexes({
//        @CompoundIndex(name = "ci_local_country_title", def = "{'title' : 1}", unique = true)
//})
//public class LocalCountry extends _BasicEntity {
//    // https://gist.github.com/adhipg/1600028
//    private String title;     // India
//    private String shortCode; // IN
//    private String phoneCode; // +91
//
//    private Currency currencyDetail;
//    private Language languageDetail;
//
//}