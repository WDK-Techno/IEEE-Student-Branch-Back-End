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

    public static UserRoleDetailsDTO convertToUserRoleDTO(UserRoleDetails userRoleDetails) {
        return UserRoleDetailsDTO.builder()
                .userRoleDetailsId(userRoleDetails.getUserRoleDetailsId())
                .start_date(userRoleDetails.getStart_date())
                .end_date(userRoleDetails.getEnd_date())
                .isActive(userRoleDetails.getIsActive())
                .type(userRoleDetails.getType())
                .role(userRoleDetails.getRole())
                .user(UserDTO.convertToDTO(userRoleDetails.getUser()))
                .ou(userRoleDetails.getOu())
                .project(userRoleDetails.getProject())
                .build();
    }
}
