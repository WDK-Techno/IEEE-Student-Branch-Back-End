package com.IEEEUWUSB.IEEEStudentBranchBackEnd.service;


import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.Role;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.User;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.repo.RoleRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class RoleServices {
    @Autowired
    RoleRepo roleRepo;

    public Role CreateRole(Role role) {
        return roleRepo.save(role);
    }

    public Page<Role> getAllRole(Integer page, String search, String type) {
        Pageable pageable = PageRequest.of(page, 15);
        return roleRepo.findAllRoles(search, type, pageable);
    }


    public boolean alreadyExistsRole(Role role) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("userRole", ExampleMatcher.GenericPropertyMatchers.ignoreCase())
                .withIgnorePaths("id");
        Example<Role> example = Example.of(role, matcher);
        try {
            roleRepo.findOne(example);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public Role getRoleById(int roleId) {
        return roleRepo.findById(roleId).get();
    }

    public Role getRoleByIdNull(int roleId) {
        Optional<Role> optionalRole = roleRepo.findById(roleId);
        return optionalRole.orElse(null);
    }


    public Role getRoleByName(String name) {
        Optional<Role> optionalRole = roleRepo.findByuserRole(name);
        return optionalRole.orElse(null);
    }


    public String updateRole(Role role) {
        try {
            getRoleById(role.getRoleID());
            try {
                roleRepo.save(role);
                return "Role updated successfully";
            } catch (Exception e) {
                return "Role Edited failed";
            }
        } catch (Exception e) {
            return "Role Not Found";
        }
    }

    public String deleteRole(int roleID) {
        if (roleRepo.existsById(roleID)) {
            roleRepo.deleteById(roleID);
            return "Role deleted successfully";
        } else {
            return "Role Not Found";
        }
    }
}
