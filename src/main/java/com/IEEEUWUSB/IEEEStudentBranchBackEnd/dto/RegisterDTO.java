package com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RegisterDTO {

    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String nameWithInitial;
    private String contactNo;
}
