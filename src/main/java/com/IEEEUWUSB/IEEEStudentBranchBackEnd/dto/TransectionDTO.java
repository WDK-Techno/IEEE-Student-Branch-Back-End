package com.IEEEUWUSB.IEEEStudentBranchBackEnd.dto;


import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.Account;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.Wallet;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TransectionDTO {
    private String title;

    private String description;

    private String type;

    private Double amount;

    private Integer to_wallet_id;

    private Integer wallet_id;

    private Integer account_id;

}
