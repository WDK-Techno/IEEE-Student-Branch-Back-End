package com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@Table(name = "user_role_project")
public class UserRoleProject {
    @EmbeddedId
    private UserRoleProjectId id = new UserRoleProjectId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roleId")
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("projectId")
    private Project project;

    public UserRoleProject(User user, Role role, Project project) {
        this.user = user;
        this.role = role;
        this.project = project;
        this.id = new UserRoleProjectId(user.getUserID(), role.getRoleID(), project.getProjectID());
    }
}
