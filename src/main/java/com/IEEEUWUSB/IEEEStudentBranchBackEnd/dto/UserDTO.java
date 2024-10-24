package com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto;


import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.AcademicYear;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserDTO {
    private String email;
    private String ieee_email;
    private String firstName;
    private String lastName;
    private String ieee_membership_number;
    private String nameWithInitial;
    private String contactNo;
    private String bio;
    private String profilePic;
    private String fbURL;
    private String linkedInURL;
    private String insterURL;
    private String location;


}



