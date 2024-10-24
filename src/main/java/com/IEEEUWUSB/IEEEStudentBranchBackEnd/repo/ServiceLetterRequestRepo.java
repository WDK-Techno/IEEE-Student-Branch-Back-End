package com.IEEEUWUSB.IEEEStudentBranchBackEnd.repo;

import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.AcademicYear;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.ServiceLetterRequest;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ServiceLetterRequestRepo extends JpaRepository<ServiceLetterRequest, Integer> {


    @Query("SELECT slr FROM ServiceLetterRequest slr " +
            "WHERE slr.user = :user " +
            "ORDER BY slr.request_date DESC")
    Page<ServiceLetterRequest> findServiceRequestByUser(User user, Pageable pageable);

    @Query("SELECT request FROM ServiceLetterRequest request " +
            "WHERE (:search IS NULL " +
            "OR " +
            "request.user.firstName LIKE CONCAT(:search, '%') " +
            "OR " +
            "request.user.lastName LIKE CONCAT(:search, '%')" +
            "OR " +
            "request.user.email LIKE CONCAT(:search, '%') " +
            "OR " +
            "request.user.ieee_email LIKE CONCAT(:search, '%'))" +
            "AND (:status IS NULL OR request.status LIKE CONCAT(:status, '%')) " +
            "ORDER BY request.request_date DESC")
    Page<ServiceLetterRequest> findAllServiceRequests(String search, String status, Pageable pageable);

}
