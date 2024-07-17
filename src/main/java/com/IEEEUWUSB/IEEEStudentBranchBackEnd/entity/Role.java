package com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int roleID;

    @Column(unique = true, nullable = false)
    private String userRole;

    @Column(nullable = false)
    private String type;

    @ManyToMany
    private Set<Policy> policies = new HashSet<>();

    public void addPolicy(Policy policy) {
        policies.add(policy);

    }

    public void removePolicy(Policy policy) {
        policies.remove(policy);

    }


}
