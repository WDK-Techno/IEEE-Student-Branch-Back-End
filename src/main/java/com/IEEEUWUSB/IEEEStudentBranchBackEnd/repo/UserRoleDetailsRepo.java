package com.IEEEUWUSB.IEEEStudentBranchBackEnd.repo;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRoleDetailsRepo extends JpaRepository<UserRoleDetails,Integer> {
    @Query("SELECT urd FROM UserRoleDetails urd WHERE urd.user = :user AND urd.isActive = :isActive AND urd.type = :type")
    Optional<UserRoleDetails> findByUserAndIsActiveAndType(User user, boolean isActive, String type);

    @Query("SELECT urd FROM UserRoleDetails urd WHERE urd.user = :user AND urd.isActive = :isActive AND (urd.type = :type OR urd.type = :type2)")
    Optional<List<UserRoleDetails>> findByUserAndIsActiveAndTypeExom(User user, boolean isActive, String type, String type2);

    @Query("SELECT urd FROM UserRoleDetails urd WHERE urd.user = :user AND urd.isActive = true AND urd.type = 'EXCOM'")
    List<UserRoleDetails> findByExcomByUser(User user);


    Optional<List<UserRoleDetails>> findByUserAndIsActive(User user, boolean isActive);

    Optional<List<UserRoleDetails>> findByUserAndIsActiveAndTypeAndProject_ProjectID(User user, boolean isActive, String type, Integer projectID);

    Optional<UserRoleDetails> findUserRoleDetailsByUserAndIsActiveAndTypeAndProject_ProjectID(User user, boolean isActive, String type, Integer projectID);

    Optional<List<UserRoleDetails>> findByUserAndIsActiveAndProject(User user, boolean isActive, Project project);

    Optional<List<UserRoleDetails>> findByRoleAndIsActiveAndProject(Role role, boolean isActive, Project project);


    @Query("SELECT urd FROM UserRoleDetails urd " +
            "WHERE (:termyearId IS NOT NULL OR urd.isActive = true) " +
            "AND urd.type = 'EXCOM' " +
            "AND (:search IS NULL OR urd.user.firstName LIKE CONCAT('%', :search, '%') " +
            "OR urd.user.lastName LIKE CONCAT('%', :search, '%') " +
            "OR urd.user.contactNo LIKE CONCAT('%', :search, '%')) " +
            "AND (:ouid IS NULL OR urd.ou.ouID = :ouid) " +
            "AND (:termyearId IS NULL OR urd.termyear.termyearId = :termyearId) " +  // Fixed this line
            "ORDER BY urd.role.priorityMain, urd.role.prioritySub")
    Page<UserRoleDetails> findAllExcomList(String search, Integer ouid, Integer termyearId, Pageable pageable);




    @Query("SELECT urd FROM UserRoleDetails urd WHERE urd.role = :role AND urd.isActive = :isActive AND urd.type = :type")
    Optional<List<UserRoleDetails>> findByUserRoleAndIsActiveAndTypeExom(Role role, boolean isActive, String type);

}

