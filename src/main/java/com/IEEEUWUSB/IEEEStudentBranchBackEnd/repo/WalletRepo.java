package com.IEEEUWUSB.IEEEStudentBranchBackEnd.repo;

import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.Project;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepo extends JpaRepository<Wallet, Integer> {
}
