package com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class UserRoleProjectId implements Serializable {
    private int userId;
    private int roleId;
    private int projectId;

    public UserRoleProjectId() {
    }

    public UserRoleProjectId(int userId, int roleId, int projectId) {
        this.userId = userId;
        this.roleId = roleId;
        this.projectId = projectId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRoleProjectId that = (UserRoleProjectId) o;
        return userId == that.userId && roleId == that.roleId && projectId == that.projectId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, roleId, projectId);
    }
}
