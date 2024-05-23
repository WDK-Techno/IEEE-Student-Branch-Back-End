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
@Table(name = "User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userID;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(columnDefinition = "TEXT",nullable = false)
    private String password;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String nameWithInitial;
    @Column(nullable = false)
    private String contactNo;
    @Column(columnDefinition = "TEXT")
    private String bio;
    @Column(columnDefinition = "varchar(255) default 'default_profile_pic.png'")
    private String profilePic;
    private String status;
    @Column(columnDefinition = "DATE")
    private Date createdDate;
    @Column(columnDefinition = "TEXT")
    private String fbURL;
    @Column(columnDefinition = "TEXT")
    private String linkedInURL;
    @Column(columnDefinition = "TEXT")
    private String insterURL;

    @ManyToOne //many users can in one academic year
    @JoinColumn(name = "academic_id")
    private AcademicYear academicYear;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

}
