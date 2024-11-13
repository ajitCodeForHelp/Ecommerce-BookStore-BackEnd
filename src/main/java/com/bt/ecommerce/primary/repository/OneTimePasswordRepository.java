package com.bt.ecommerce.primary.repository;

import com.bt.ecommerce.primary.pojo.OneTimePassword;
import com.bt.ecommerce.primary.pojo.enums.VerificationTypeEnum;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface OneTimePasswordRepository extends MongoRepository<OneTimePassword, Long> {

    @Query(value = "{" +
            "  'otpSentOn'  : ?0," +
            "  'otpCode' : ?1," +
            "  'verificationType' : ?2," +
            "  'expiredAt' : { '$gt' : ?3 }," +
            "  'expired' : false" +
            "}")
    OneTimePassword verifyOtp(String otpSentOn, String otpCode,
                                    VerificationTypeEnum verificationType, LocalDateTime time);

    @Query(value = "{" +
            "  'otpSentOn'  : ?0," +
            "  'expired' : false" +
            "}")
    List<OneTimePassword> findAllNonExpiredOtp(String mobile);
}
