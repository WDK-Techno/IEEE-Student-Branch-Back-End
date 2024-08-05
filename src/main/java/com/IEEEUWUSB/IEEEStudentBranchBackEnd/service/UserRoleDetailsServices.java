package com.IEEEUWUSB.IEEEStudentBranchBackEnd.service;


import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.Policy;
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


    public UserRoleDetails getuserRoleDetailsExom(User user,boolean isActive, String type,String type2) {
        Optional<UserRoleDetails> optionalRole = userRoleDetailsRepo.findByUserAndIsActiveAndTypeExom(user,isActive,type,type2);
        return optionalRole.orElse(null);
    }

    public boolean isPolicyAvailable(UserRoleDetails userData, String policyCode) {
        if (userData != null && userData.getRole() != null) {
            Role role = userData.getRole();  // Assuming there's only one role
            if (role.getPolicies() != null) {
                for (Policy policy : role.getPolicies()) {
                    if (policyCode.equals(policy.getPolicyCode())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


}
