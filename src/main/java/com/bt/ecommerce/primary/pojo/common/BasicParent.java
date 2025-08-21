package com.bt.ecommerce.primary.pojo.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BasicParent {

    private String parentUuid;
    private String parentTitle;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasicParent that = (BasicParent) o;
        return Objects.equals(parentUuid, that.parentUuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parentUuid);
    }

}
