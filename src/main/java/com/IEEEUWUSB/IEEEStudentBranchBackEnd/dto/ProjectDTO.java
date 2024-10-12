package com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto;

import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.OU;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.TermYear;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProjectDTO {

    private String project_name;

    private String description;

    private Date start_date;

    private Date end_date;

    private String project_logo;

    private String status;

    private Integer[] ou_id;


}
