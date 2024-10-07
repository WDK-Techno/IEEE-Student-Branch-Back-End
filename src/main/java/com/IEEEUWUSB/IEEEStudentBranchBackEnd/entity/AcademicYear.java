package com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "academic")
public class AcademicYear {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int acedemicId;

    @Column(nullable = false)
    private String enrolledBatch;

    @Column(unique = true, nullable = false)
    private String academicYear;

    @Column(nullable = false)
    private String status;

}
