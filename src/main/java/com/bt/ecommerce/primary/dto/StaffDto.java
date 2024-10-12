package com.bt.ecommerce.primary.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class StaffDto extends AbstractDto{

    @Setter
    @Getter
    public static class SaveStaff extends Save {
        @NotNull private String jobRole;

        @NotNull private String firstName;
        @NotNull private String lastName;
        private String isdCode;
        private String mobile;
        private String email;
        @NotNull private String password;
        private String photoImageUrl;
    }
    @Setter
    @Getter
    public static class UpdateStaff extends Update {
        @NotNull private String jobRole;

        @NotNull private String firstName;
        @NotNull private String lastName;
        private String isdCode;
        private String mobile;
        private String email;
        @NotNull private String password;
        private String photoImageUrl;
    }
    @Setter
    @Getter
    public static class DetailStaff extends Detail {
        private String jobRole;

        private String firstName;
        private String lastName;
        private String isdCode;
        private String mobile;
        private String email;
        private String photoImageUrl;
    }

    @Setter
    @Getter
    public static class GetList extends _BasePageRequest {
        private String search;
    }




}
