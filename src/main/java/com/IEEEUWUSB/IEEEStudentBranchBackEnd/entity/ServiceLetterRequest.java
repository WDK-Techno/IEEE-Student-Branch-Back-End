package com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "service")
public class ServiceLetterRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int serviceId;

    private String remarks;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String status;
    @Column(nullable = false)
    private String type;
    @Column(nullable = false)
    private LocalDate request_date;
    @Column(nullable = false)
    private Date due_date;
    @Column(nullable = false)
    private LocalDateTime reviewed_date;
    @ManyToOne
    private User reviewed_by;
    @ManyToOne
    private User user;

}
