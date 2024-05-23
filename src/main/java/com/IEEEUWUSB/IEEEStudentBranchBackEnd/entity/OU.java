package com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "ou")
public class OU {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ouID;

    @Column(nullable = false)
    private String ouName;

    @Column(columnDefinition = "varchar(255) default 'default_ou_pic.png'")
    private String ou_logo;

    @ManyToMany(mappedBy = "ouList")
    private List<Project> projects;

    @OneToMany(mappedBy = "ou", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserRoleOu> userRoleOus;


}
