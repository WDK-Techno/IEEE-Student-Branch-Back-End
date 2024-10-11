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

    public static UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .email(user.getEmail())
                .ieee_email(user.getIeee_email())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .ieee_membership_number(user.getIeee_membership_number())
                .nameWithInitial(user.getNameWithInitial())
                .contactNo(user.getContactNo())
                .bio(user.getBio())
                .profilePic(user.getProfilePic())
                .fbURL(user.getFbURL())
                .linkedInURL(user.getLinkedInURL())
                .insterURL(user.getInsterURL())
                .location(user.getLocation())
                .build();
    }


}



