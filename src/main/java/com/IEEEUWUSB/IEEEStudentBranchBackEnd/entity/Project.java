package com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "project")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int projectID;

    @Column(nullable = false)
    private String projectName;

    private String description;

    private Date startDate;

    private Date endDate;

    @Column(columnDefinition = "varchar(255) default 'default_project_pic.png'")
    private String projectLogo;

    private String status;

    @ManyToOne
    private TermYear termyear;

    @ManyToMany
    private Set<OU> ou = new HashSet<>();


    @ManyToOne
    private User createdBy;

    @ManyToMany
    @JoinTable(
            name = "priject_user",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users;

    public void adduser(User user) {
        users.add(user);
    }

    public void removeuser(User user) {
        users.remove(user);
    }

    public void addOU(OU ou) {
        this.ou.add(ou);
    }

    public void removeOU(OU ou) {
        this.ou.remove(ou);
    }





}
