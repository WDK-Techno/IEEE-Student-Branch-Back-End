package com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "project")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int projectID;

    @Column(nullable = false)
    private String projectName;

    private String description;

    private Date startDate;

    private Date endDate;

    @Column(columnDefinition = "varchar(255) default 'default_project_pic.png'")
    private String projectLogo;

    private String status;

    @ManyToMany
    @JoinTable(
            name = "project_ou",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "ou_id")
    )
    private List<OU> ouList;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserRoleProject> userRoleProjects;

}
