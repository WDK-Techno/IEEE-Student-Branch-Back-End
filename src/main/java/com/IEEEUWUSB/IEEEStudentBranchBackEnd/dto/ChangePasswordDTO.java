package com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ChangePasswordDTO {
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
}
