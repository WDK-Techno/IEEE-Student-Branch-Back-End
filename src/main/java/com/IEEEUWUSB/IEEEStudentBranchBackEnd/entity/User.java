package com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userID;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String nameWithInitial;
    private String contactNo;
    private String bio;
    private String profilePic;
    private String status;
    private String createdDate;
    private String fbURL;
    private String linkedInURL;
    private String insterURL;
    private int academicID;
    private int roleID;

}
