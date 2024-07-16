package com.IEEEUWUSB.IEEEStudentBranchBackEnd.service;


import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.Role;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.User;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.UserRoleDetails;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.repo.UserRoleDetailsRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class UserRoleDetailsServices {
    @Autowired
    UserRoleDetailsRepo userRoleDetailsRepo;

    public UserRoleDetails createUserRoleDetails(UserRoleDetails userRoleDetails) {
        return userRoleDetailsRepo.save(userRoleDetails);
    }

    public UserRoleDetails getuserRoleDetails(User user,boolean isActive, String type) {
        Optional<UserRoleDetails> optionalRole = userRoleDetailsRepo.findByUserAndIsActiveAndType(user,isActive,type);
        return optionalRole.orElse(null);
    }

}
