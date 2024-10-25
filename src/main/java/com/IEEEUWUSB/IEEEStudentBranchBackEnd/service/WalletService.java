package com.IEEEUWUSB.IEEEStudentBranchBackEnd.service;


import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.OU;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.Project;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.Wallet;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.repo.ProjectRepo;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.repo.WalletRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class WalletService {

    @Autowired
    WalletRepo walletRepo;

    public Wallet saveWallet(Wallet wallet) {
        return walletRepo.save(wallet);
    }

    public Wallet getWalletById(Integer id) {
        Optional<Wallet> optionalRole = walletRepo.findById(id);
        return optionalRole.orElse(null);
    }

    public List<Wallet> getAllWallet() {
        return walletRepo.findAll();
    }

    public List<Wallet> getAllExcomWallet(String type) {
        return walletRepo.findByType(type);
    }

    public Wallet getSBWallet(String type) {
        return walletRepo.findFirstByType(type);
    }

    public Wallet getWalletByOU(OU ou) {
        return walletRepo.findByOu(ou);
    }

    public Wallet getWalletByProoject(Project project) {
        return walletRepo.findByProject(project);
    }
}
