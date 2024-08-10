package com.IEEEUWUSB.IEEEStudentBranchBackEnd.repo;

import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.Policy;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    @Query("SELECT user FROM User user " +
            "WHERE (:search IS NULL " +
            "OR " +
            "user.email LIKE CONCAT(:search, '%') " +
            "OR " +
            "user.firstName LIKE CONCAT(:search, '%') " +
            "OR " +
            "user.lastName LIKE CONCAT(:search, '%') " +
            "OR " +
            "user.ieee_email LIKE CONCAT(:search, '%')) " +
            "ORDER BY user.userID DESC")
    Page<User> finduser(String search, Pageable pageable);
}
