package com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "user_role_details")
public class UserRoleDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int userRoleDetailsId;

    private Date start_date;

    private Date end_date;

    private Boolean isActive;

   @ManyToOne
   private Role role;

   @ManyToOne
   private User user;

   @ManyToOne
   private OU ou;

   @ManyToOne
   private Project project;


}
