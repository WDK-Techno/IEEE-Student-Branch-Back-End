package com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TaskCreateDTO {
    private String task_name;
    private String status;
    private String type;
    private Date start_date;
    private Date end_date;
    private Integer ou_id;
    private String priority;



}
