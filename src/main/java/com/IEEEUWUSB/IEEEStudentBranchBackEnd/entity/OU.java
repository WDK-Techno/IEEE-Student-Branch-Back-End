package com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "ou")
public class OU {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int ouID;

    @Column(nullable = false)
    private String ouName;

    @Column(columnDefinition = "varchar(255) default 'default_ou_pic.png'")
    private String ou_logo;

    @Column(nullable = false)
    private String ou_short_name;





}
