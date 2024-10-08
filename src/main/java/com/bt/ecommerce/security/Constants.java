package com.bt.ecommerce.security;

public class Constants {

    public static final String[] IGNORE_PATTERNS = new String[]{"**/*.js", "**/*.css", "/resources/**", "/images/**", "/css/**", "/js/**"};
    public static final String[] PERMIT_ALL_PATTERNS = new String[]{
            "/auth/**",
            "/public/**",
            "/guest/**",
            "/",

            "/swagger-ui/index.html",
            "/swagger*/**",
            "/*/api-docs/**",
            "/api-docs",
            "/api-docs/**",

            "/.well-known/**",
            "/apple-app-site-association",

            "/website/**",
            "/*/api/guest/**",
            "/webjars/springfox-swagger-ui/**",
            "/csrf",
    };
    public static final String[] MONITORING_PATTERNS = new String[]{"/actuator/**"};
    public static final String[] PROTECTED_PATTERNS = new String[]{"/super-admin/**", "/admin/**", "/client/**"};
}
