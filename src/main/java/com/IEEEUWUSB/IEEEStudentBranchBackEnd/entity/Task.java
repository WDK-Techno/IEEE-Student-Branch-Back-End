package com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int taskId;

    @Column(nullable = false)
    private String task_name;

    private String description;

    @Column(nullable = false)
    private String status;

    private Date start_date;

    @Column(nullable = false)
    private String type;

    private Date end_date;

    private String priority;

    @ManyToOne
    private Project project;

    @ManyToOne
    private OU ou;

    @ManyToOne
    private Task parentTask;

    @OneToMany
    private Set<Task> subtasks;

    @ManyToMany
    @JoinTable(
            name = "task_user",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users;

    @ManyToOne
    private User createdBy;


    public void adduser(User user) {
        users.add(user);
    }

    public void removeuser(User user) {
        users.remove(user);
    }

    public void addsubtask(Task task) {
        subtasks.add(task);
    }

    public void removesubtask(Task task) {
        subtasks.remove(task);
    }




}
