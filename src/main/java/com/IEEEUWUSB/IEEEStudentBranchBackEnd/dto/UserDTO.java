package com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
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
}
