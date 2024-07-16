package com.IEEEUWUSB.IEEEStudentBranchBackEnd.service;


import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.UserRoleDetails;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.repo.UserRoleDetailsRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserRoleDetailsServices {
    @Autowired
    UserRoleDetailsRepo userRoleDetailsRepo;

    public UserRoleDetails createUserRoleDetails(UserRoleDetails userRoleDetails) {
        return userRoleDetailsRepo.save(userRoleDetails);
    }
}
