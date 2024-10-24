package com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto;

import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.*;
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
public class UserRoleDetailsDTO {

    private int userRoleDetailsId;
    private LocalDateTime start_date;
    private LocalDateTime end_date;
    private Boolean isActive;
    private String type;
    private Role role;
    private UserDTO user;
    private OU ou;
    private Project project;

}
