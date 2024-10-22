package com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto;


import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ServiceLetterReqDTO {


    private String remarks;
    private String email;
    private String type;
    private Date due_date;
}
