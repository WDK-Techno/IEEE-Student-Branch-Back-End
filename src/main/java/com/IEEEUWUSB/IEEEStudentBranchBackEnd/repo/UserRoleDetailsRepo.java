package com.IEEEUWUSB.IEEEStudentBranchBackEnd.repo;

import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.User;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.UserRoleDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRoleDetailsRepo extends JpaRepository<UserRoleDetails,Integer> {
    @Query("SELECT urd FROM UserRoleDetails urd WHERE urd.user = :user AND urd.isActive = :isActive AND urd.type = :type")
    Optional<UserRoleDetails> findByUserAndIsActiveAndType(User user, boolean isActive, String type);

    @Query("SELECT urd FROM UserRoleDetails urd WHERE urd.user = :user AND urd.isActive = :isActive AND (urd.type = :type1 OR urd.type = :type2)")
    Optional<UserRoleDetails> findByUserAndIsActiveAndTypeExom(User user, boolean isActive, String type,String type2);
}
