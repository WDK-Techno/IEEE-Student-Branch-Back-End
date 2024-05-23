package com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@Table(name = "user_role_ou")
public class UserRoleOu {
    @EmbeddedId
    private UserRoleOuId id = new UserRoleOuId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roleId")
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("ouId")
    private OU ou;

    public UserRoleOu(User user, Role role, OU ou) {
        this.user = user;
        this.role = role;
        this.ou = ou;
        this.id = new UserRoleOuId(user.getUserID(), role.getRoleID(), ou.getOuID());
    }
}
