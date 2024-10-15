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
public class EventDTO {
    private String eventName;

    private String status;

    private String eventLink;

    private Date date;

    private String venue;

    private String image;

    private String decsription;
}
