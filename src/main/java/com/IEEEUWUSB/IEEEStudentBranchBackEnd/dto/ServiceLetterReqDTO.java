package com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto;


import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ServiceLetterReqDTO {


    private String remarks;
    private String email;
    private Boolean type_excom;
    private Boolean type_project;
    private Boolean type_other;
    private Date due_date;
}
