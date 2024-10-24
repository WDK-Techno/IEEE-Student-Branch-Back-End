package com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto;

import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BestVolunteerDTO {

    private User user;
    private Long completedTaskCount;
    private Long enrolledProjectCount;
}
