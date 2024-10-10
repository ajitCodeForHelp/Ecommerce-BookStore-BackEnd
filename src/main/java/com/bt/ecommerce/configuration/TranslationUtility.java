package com.bt.ecommerce.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;

@Configuration
public class TranslationUtility {

    // Translation
    @Autowired
    private ResourceBundleMessageSource messageSource;

    public String getMessage(String pMessageKey) {
        Locale locale = Locale.getDefault();
        if (pMessageKey.startsWith("ecommerce")) {
            try {
                return messageSource.getMessage(pMessageKey, null, locale);
            } catch (Exception e) {
            }
        }
        return pMessageKey;
    }


}