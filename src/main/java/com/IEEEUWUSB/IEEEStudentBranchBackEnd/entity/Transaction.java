package com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "reference_id", nullable = false, unique = true)
    private String referenceId;

    private String title;

    private String description;

    private String type;

    private LocalDateTime date;

    private Double amount;

    @ManyToOne
    private Wallet from_wallet;

    @ManyToOne
    private Wallet to_wallet;

    @ManyToOne
    private Wallet wallet;

    @ManyToOne
    private Account account;





    @PrePersist
    private void generateReferenceId() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        this.referenceId = "TXN-" + LocalDateTime.now().format(formatter) + "-" + id;
    }
}
