package com.IEEEUWUSB.IEEEStudentBranchBackEnd.repo;

import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface TransectionRepo extends JpaRepository<Transaction, Integer> {

    @Query("SELECT transaction FROM Transaction transaction " +
            "WHERE (:search IS NULL " +
            "OR transaction.referenceId LIKE CONCAT(:search, '%') " +
            "OR transaction.title LIKE CONCAT(:search, '%')) " +
            "AND (:wallet IS NULL OR transaction.wallet = :wallet) " +
            "AND (:type IS NULL OR :type = '' OR transaction.type = :type) " + // Allow for null or empty string
            "AND (:startDate IS NULL OR transaction.date >= :startDate) " + // Handle startDate
            "AND (:endDate IS NULL OR transaction.date <= :endDate)" +
            "ORDER BY transaction.id DESC")
    Page<Transaction> findByWallet(String search, Wallet wallet, String type, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);



    @Query("SELECT transaction FROM Transaction transaction " +
            "WHERE (:search IS NULL " +
            "OR transaction.referenceId LIKE CONCAT(:search, '%') " +
            "OR transaction.title LIKE CONCAT(:search, '%')) " +
            "AND (:account IS NULL OR transaction.account = :account) " +
            "AND (:type IS NULL OR :type = '' OR transaction.type = :type) " + // Allow for null or empty string
            "AND (:startDate IS NULL OR transaction.date >= :startDate) " + // Handle startDate
            "AND (:endDate IS NULL OR transaction.date <= :endDate)" +
            "ORDER BY transaction.id DESC")
    Page<Transaction> findByAccount(String search, Account account, String type, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);


}
