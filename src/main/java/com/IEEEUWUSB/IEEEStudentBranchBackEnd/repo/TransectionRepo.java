package com.IEEEUWUSB.IEEEStudentBranchBackEnd.repo;

import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

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


    // Query to calculate total credit value for a specific account
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.account.id = :accountId AND t.type = 'CREDIT'")
    Optional<Double> getTotalCreditByAccountId(@Param("accountId") int accountId);

    // Query to calculate total debit value for a specific account
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.account.id = :accountId AND t.type = 'DEBIT'")
    Optional<Double> getTotalDebitByAccountId(@Param("accountId") int accountId);

    // Query to calculate total credit value for a specific account
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.wallet.id = :walletId AND t.type = 'CREDIT'")
    Optional<Double> getTotalCreditBywalletId(@Param("walletId") int walletId);

    // Query to calculate total debit value for a specific account
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.wallet.id = :walletId AND t.type = 'DEBIT'")
    Optional<Double> getTotalDebitBywalletId(@Param("walletId") int walletId);




}
