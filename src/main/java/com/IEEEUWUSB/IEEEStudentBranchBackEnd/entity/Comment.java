package com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int commentID;

    private String comment;

    private LocalDateTime created_at;


    @ManyToOne
    private Project project;

    @ManyToOne
    private User user;


    @ManyToOne
    private Task task;
}
