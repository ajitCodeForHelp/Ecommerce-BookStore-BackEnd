package com.bt.ecommerce.primary.pojo.user;

import com.bt.ecommerce.primary.pojo._BasicEntity;
import com.bt.ecommerce.primary.pojo.enums.GenderEnum;
import com.bt.ecommerce.primary.pojo.enums.RoleEnum;
import com.bt.ecommerce.utils.TextUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

@Setter
@Getter
public class _BaseUser extends _BasicEntity implements UserDetails {

    protected String firstName;
    protected String lastName;

    protected GenderEnum gender;

    protected LocalDate dateOfBirth;

    protected String isdCode;
    protected String mobile;
    protected boolean mobileVerified = false;
    protected String email;
    protected boolean emailVerified = false;
    protected String username;
    protected String pwdSecure;
    protected String pwdText;

    /**
     * @userType > ROLE_SUPER_ADMIN / ROLE_ADMIN
     */
    protected RoleEnum userType;

    protected String walletReferenceId;
    protected String photoImageUrl;

    protected String uniqueKey;

    protected Boolean forceUpdatePassword = true;

    protected Boolean agreeTNC = false;
    protected LocalDateTime agreeTNCDate;
    protected String agreeTNCIP;
    protected String agreeTNCDeviceUuid;
    protected String agreeTNCDeviceType;
    protected Double agreeTNCLatitude;
    protected Double agreeTNCLongitude;
    protected LocalDateTime lastLogin;

    // Lang = "en" / "hi" / "ar"

    public String fullName() {
        return (
                (TextUtils.isEmpty(firstName) ? "" : firstName) +
                        (TextUtils.isEmpty(lastName) ? "" : " " + lastName)
        ).trim();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return pwdSecure;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return isActive();
    }

    public String getUsername() {
        if (!TextUtils.isEmpty(username)) {
            return username;
        } else if (!TextUtils.isEmpty(email)) {
            return email;
        } else if (!TextUtils.isEmpty(mobile)) {
            return mobile;
        }
        return null;
    }
}
