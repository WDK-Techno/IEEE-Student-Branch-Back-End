package com.IEEEUWUSB.IEEEStudentBranchBackEnd.repo;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepo extends JpaRepository<Project, Integer> {
    Page<Project> findByProjectNameOrStatusOrOusContainingOrTermyear(String projectName, String status, OU ou, TermYear termYear, Pageable pageable);
    Page<Project> findByProjectNameOrStatusOrUsersContainingOrCreatedBy(String projectName, String status,User user,User createdby, Pageable pageable);
}

