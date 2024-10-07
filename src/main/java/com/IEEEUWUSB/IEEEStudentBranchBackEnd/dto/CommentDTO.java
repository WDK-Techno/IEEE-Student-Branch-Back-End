package com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CommentDTO {
    private String comment;
    private Integer project_id;
    private Integer task_id;
    private String type;
}
