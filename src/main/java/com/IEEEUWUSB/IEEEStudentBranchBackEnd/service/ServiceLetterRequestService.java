package com.IEEEUWUSB.IEEEStudentBranchBackEnd.service;

import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.ServiceLetterRequest;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.repo.ServiceLetterRequestRepo;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.repo.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ServiceLetterRequestService {
    @Autowired
    ServiceLetterRequestRepo serviceLetterRequestRepo;

    @Autowired
    UserRepo userRepo;

    public ServiceLetterRequest saveServiceLetterRequest(ServiceLetterRequest serviceLetterRequest) {
        return serviceLetterRequestRepo.save(serviceLetterRequest);
    }
}
