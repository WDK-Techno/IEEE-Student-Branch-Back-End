package com.IEEEUWUSB.IEEEStudentBranchBackEnd.repo;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProjectRepo extends JpaRepository<Project, Integer> {

    @Query("SELECT project FROM Project project " +
            "WHERE (:projectName IS NULL OR project.projectName LIKE CONCAT(:projectName, '%')) " +
            "AND (:status IS NULL OR project.status LIKE CONCAT(:status, '%')) " +
            "AND (:ou IS NULL OR :ou MEMBER OF project.ous) " +
            "AND (:termYear IS NULL OR project.termyear = :termYear) " +
            "ORDER BY project.projectID DESC")
    Page<Project> findAllProjects(String projectName, String status, OU ou, TermYear termYear, Pageable pageable);

    @Query("SELECT project FROM Project project " +
            "WHERE (:projectName IS NULL OR project.projectName LIKE CONCAT(:projectName, '%')) " +
            "AND (:status IS NULL OR project.status LIKE CONCAT(:status, '%')) " +
            "AND (:ou MEMBER OF project.ous) " +
            "AND (:termYear IS NULL OR project.termyear = :termYear) " +
            "ORDER BY project.projectID DESC")
    Page<Project> findAllProjectsByExom(String projectName, String status, OU ou, TermYear termYear, Pageable pageable);

    @Query("SELECT project FROM Project project " +
            "WHERE (:projectName IS NULL OR project.projectName LIKE CONCAT(:projectName, '%')) " +
            "AND (:status IS NULL OR project.status LIKE CONCAT(:status, '%')) " +
            "AND ((:user IS NULL OR :user MEMBER OF project.users) " +
            "OR (:createdby IS NULL OR project.createdBy = :createdby)) " +
            "ORDER BY project.projectID DESC")
    Page<Project> findProjectsByUser(String projectName, String status,User user,User createdby, Pageable pageable);

    long countByStatusAndOusContainingAndTermyear(
            String status,
            OU ou,
            TermYear termYear
    );

    long countByStatusAndOusContaining(
            String status,
            OU ou
    );


    long countByStatusAndTermyear(
            String status,
            TermYear termYear
    );

    long countByStatus(
            String status
    );

    // For counting by user
    @Query("SELECT COUNT(p) FROM Project p " +
            "WHERE ( :user MEMBER OF p.users OR p.createdBy = :createdby ) AND p.status = :status")
    long countByStatusAndUsersOrCreatedBy(
            String status,
            User user,
            User createdby
    );



}

