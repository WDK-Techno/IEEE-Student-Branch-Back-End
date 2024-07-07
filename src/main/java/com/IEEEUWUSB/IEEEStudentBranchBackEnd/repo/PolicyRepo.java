package com.IEEEUWUSB.IEEEStudentBranchBackEnd.repo;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.Policy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PolicyRepo extends JpaRepository<Policy, Integer> {

    @Query("SELECT pl FROM Policy pl " +
            "WHERE (:search IS NULL OR pl.policy LIKE CONCAT(:search, '%') OR pl.policyCode LIKE CONCAT(:search, '%')) " +
            "AND (:type IS NULL OR pl.type LIKE CONCAT(:type, '%')) " +
            "ORDER BY pl.policyID DESC")
    Page<Policy> findAllPolicies(String search,String type, Pageable pageable);
}
