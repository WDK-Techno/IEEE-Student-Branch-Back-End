package com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "user_role_details")
public class UserRoleDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int userRoleDetailsId;

    private LocalDateTime start_date;

    private LocalDateTime end_date;

    private Boolean isActive;

    private String type;

    @ManyToOne
    private Role role;


    @ManyToOne
    private User user;

    @ManyToOne
    private OU ou;

    @ManyToOne
    private TermYear termyear;

    @ManyToOne
    private Project project;

}
