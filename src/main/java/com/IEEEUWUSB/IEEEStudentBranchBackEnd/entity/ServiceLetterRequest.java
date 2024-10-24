package com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

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
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean type_excom;
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean type_project;
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean type_other;
    @Column(nullable = false)
    private LocalDateTime request_date;
    @Column(nullable = false)
    private Date due_date;
    private LocalDateTime reviewed_date;
    @ManyToOne
    private User reviewed_by;
    @ManyToOne
    private User user;

    @PrePersist
    public void prePersist() {
        if (type_excom == null) {
            type_excom = false;
        }
        if (type_project == null) {
            type_project = false;
        }
        if (type_other == null) {
            type_other = false;
        }
    }
}
