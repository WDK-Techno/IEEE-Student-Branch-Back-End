package com.IEEEUWUSB.IEEEStudentBranchBackEnd.service;


import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.*;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.repo.TransectionRepo;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.repo.WalletRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
public class TransectionService {


    @Autowired
    TransectionRepo transectionRepo;


    public Transaction saveTransection(Transaction transaction) {
        return transectionRepo.save(transaction);
    }


    public Page<Transaction> findAllTransactionByWaltet(String search, Wallet wallet, String type, LocalDateTime startDate,LocalDateTime endDate, Integer page) {
        Pageable pageable = PageRequest.of(page, 15);
        return transectionRepo.findByWallet(search,wallet,type,startDate,endDate, pageable);
    }

    public Page<Transaction> findAllTransactionByAccount(String search, Account account, String type, LocalDateTime startDate,LocalDateTime endDate, Integer page) {
        Pageable pageable = PageRequest.of(page, 15);
        return transectionRepo.findByAccount(search,account,type,startDate,endDate, pageable);
    }



}
