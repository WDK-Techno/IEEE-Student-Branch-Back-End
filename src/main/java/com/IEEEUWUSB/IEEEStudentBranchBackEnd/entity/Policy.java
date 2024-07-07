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
@Table(name = "policy")
public class Policy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int policyID;

    @Column(nullable = false)
    private String policy;

    @Column(nullable = false)
    private String type;

    @Column(unique = true,nullable = false)
    private String policyCode;


}
