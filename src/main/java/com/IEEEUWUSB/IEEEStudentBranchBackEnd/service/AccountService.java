package com.IEEEUWUSB.IEEEStudentBranchBackEnd.service;

import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.Account;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.OU;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.Project;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.TermYear;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.repo.AccountRepo;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.repo.ProjectRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AccountService {
    @Autowired
    AccountRepo accountRepo;

    public Account saveAccount(Account account) {
        return accountRepo.save(account);
    }

    public Account getAccountById(Integer id) {
        Optional<Account> optionalRole = accountRepo.findById(id);
        return optionalRole.orElse(null);
    }

    public void deleteAccount(Account account){
        accountRepo.delete(account);
    }

    public List<Account> getAllAccount() {
        return accountRepo.findAll();
    }
}
