package com.IEEEUWUSB.IEEEStudentBranchBackEnd.service;


import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.*;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.repo.UserRoleDetailsRepo;
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


    public List<UserRoleDetails> getuserRoleDetailsExom(User user,boolean isActive, String type,String type2) {
        Optional<List<UserRoleDetails>> optionalRole = userRoleDetailsRepo.findByUserAndIsActiveAndTypeExom(user,isActive,type,type2);
        return optionalRole.orElse(null);
    }


    public List<UserRoleDetails> getuserRoleDetailsExomByUserRole(Role role,boolean isActive, String type) {
        Optional<List<UserRoleDetails>> optionalRole = userRoleDetailsRepo.findByUserRoleAndIsActiveAndTypeExom(role,isActive,type);
        return optionalRole.orElse(null);
    }




    public Page<UserRoleDetails> getAllExcomUserDetails(Integer page, String search, Integer ouid){
        Pageable pageable = PageRequest.of(page, 15);
        return userRoleDetailsRepo.findAllExcomList(search, ouid, pageable);
    }
//    public List<UserRoleDetails> getOUExcomUserDetailsByOU(OU ou){
//        return userRoleDetailsRepo.findExcomListByOu(ou);
//    }

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
