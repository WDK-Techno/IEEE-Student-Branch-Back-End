package com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto;

import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.Project;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.UserRoleDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProjectUserPermissionDTO {
    private Project project;
    private List<UserRoleDetails> my_user_role_details;
    private List<UserRoleDetails> other_role_details;
}
