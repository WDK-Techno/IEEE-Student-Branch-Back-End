package com.IEEEUWUSB.IEEEStudentBranchBackEnd.repo;

import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.Project;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.TermYear;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WalletRepo extends JpaRepository<Wallet, Integer> {

    List<Wallet> findByType(String type);
}
