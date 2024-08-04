package com.IEEEUWUSB.IEEEStudentBranchBackEnd.service;


import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.AcademicYear;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.Policy;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.entity.Role;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.repo.PolicyRepo;
import com.IEEEUWUSB.IEEEStudentBranchBackEnd.repo.RoleRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class PolicyService {

    @Autowired
    PolicyRepo policyRepo;
    @Autowired
    private RoleServices roleServices;
    @Autowired
    private RoleRepo roleRepo;

    public Policy CreatePolicy(Policy policy) {
        return policyRepo.save(policy);
    }

    public Page<Policy> getAllPolicy(Integer page, String search, String type) {
        Pageable pageable = PageRequest.of(page, 15);
        return policyRepo.findAllPolicies(search, type, pageable);
    }



    public boolean alreadyExistsPolicyCode(Policy policy) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("policyCode", ExampleMatcher.GenericPropertyMatchers.ignoreCase())
                .withIgnorePaths("id");
        Example<Policy> example = Example.of(policy, matcher);
        try {
            policyRepo.findOne(example);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public Policy getPolicyById(int policyId) {
        return policyRepo.findById(policyId).get();
    }

    public String updatePolicy(Policy policy) {
        try{
            getPolicyById(policy.getPolicyID());
            try{
                policyRepo.save(policy);
                return "Academic year updated successfully";
            }catch (Exception e){
                return "Academic Year Edited failed";
            }
        }catch (Exception e){
            return "Academic Year Not Found";
        }
    }

    public String assignPolicy(Integer roleId, Integer[] policyIds) {
        try {
            // Fetch the role by its ID
            Role role = roleServices.getRoleById(roleId);

            // Fetch all existing policies for the role
            Set<Policy> existingPolicies = new HashSet<>(role.getPolicies());

            // Convert the array of policy IDs to a set for efficient lookup
            Set<Integer> policyIdSet = new HashSet<>(Arrays.asList(policyIds));

            // Iterate over existing policies and remove those not in the new list
            for (Policy policy : existingPolicies) {
                if (!policyIdSet.contains(policy.getPolicyID())) {
                    role.removePolicy(policy);
                }
            }

            // Add new policies
            for (Integer policyId : policyIds) {
                Policy policy = getPolicyById(policyId);
                if (policy != null && !role.getPolicies().contains(policy)) {
                    role.addPolicy(policy);
                }
            }

            // Save the updated role
            roleRepo.save(role);

            return "Policies assigned successfully";
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception for debugging
            return "An error occurred while assigning policies";
        }

    }

    public Policy getPolicyBycode(String code) {
        Optional<Policy> optionalRole = policyRepo.findBypolicyCode(code);
        return optionalRole.orElse(null);
    }


}
