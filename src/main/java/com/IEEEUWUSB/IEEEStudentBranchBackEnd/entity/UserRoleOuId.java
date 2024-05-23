package com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Setter
@Getter
@Embeddable
public class UserRoleOuId implements Serializable {
    private int userId;
    private int roleId;
    private int ouId;

    public UserRoleOuId() {
    }

    public UserRoleOuId(int userId, int roleId, int ouId) {
        this.userId = userId;
        this.roleId = roleId;
        this.ouId = ouId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRoleOuId that = (UserRoleOuId) o;
        return userId == that.userId && roleId == that.roleId && ouId == that.ouId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, roleId, ouId);
    }
}
